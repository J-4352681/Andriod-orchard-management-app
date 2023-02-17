package app.lajusta.ui.familia.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentFamiliaModifyBinding
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.model.BolsonCompleto
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.API.QuintaApi
import app.lajusta.ui.rondas.Ronda
import app.lajusta.ui.rondas.api.RondaApi

class FamiliaModifyFragment: BaseFragment(){

    private var _binding: FragmentFamiliaModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var familia: Familia
    private lateinit var rondas: List<Ronda>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            familia = bundle.getParcelable("familia")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFamiliaModifyBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()
    }

    private fun fillItem() {
        binding.tvFechaSeleccionada.text = ArrayedDate.toString(familia.fecha_afiliacion)

        binding.tvTitle.text =
            ("Familia " + familia.nombre
            + " afiliada " + ArrayedDate.toString(familia.fecha_afiliacion))

        binding.etNombre.setText(familia.nombre)

        apiCall(suspend {
            // API QUINTAS
            var quintas = QuintaApi().getQuintas().body()!!
            // API RONDAS
            rondas = RondaApi().getRondas().body()!!
            var bolsones = BolsonApi().getBolsones().body()!!
            val bolsonesCompletos = mutableListOf<BolsonCompleto>()


            // PROCESAMIENTO QUINTAS
            val textQuintas = quintas
                .filter { it.fpId == familia.id_fp }
                .map { it.nombre }.toString()
            // PROCESAMIENTO RONDAS
            bolsones = bolsones.filter { it.idFp == familia.id_fp }
            bolsones
                .forEach { bolson ->
                    bolsonesCompletos.add(
                        BolsonCompleto.toBolsonCompleto(bolson, familia, rondas.filter {
                            it.id_ronda == bolson.idRonda
                        }.firstOrNull()!!)
                    )
                }
            val fechasBolsonesRondas = bolsonesCompletos.map {
                ArrayedDate.toString(it.ronda.fecha_inicio.toMutableList())
            }.toString()

            activity!!.runOnUiThread {
                // UI QUINTAS
                binding.tvQuintasList.text = textQuintas.subSequence(1, textQuintas.length-1)
                // UI RONDAS
                binding.tvUltimoBolson.text = fechasBolsonesRondas
                    .subSequence(1, fechasBolsonesRondas.length-1)
            }
        }, "No se pudieron obtener las quintas y/o bolsones de la familia.")
    }

    private fun setClickListeners() {
        binding.bFecha.setOnClickListener(ArrayedDate.datePickerListener(
            activity!!, binding.tvFechaSeleccionada
        ) )

        binding.bBorrar.setOnClickListener {
            returnSimpleApiCall(
                { FamiliaApi().deleteFamilia(familia.id_fp) },
                "Hubo un error. La familia no pudo ser eliminada."
            )
        }

        binding.bGuardar.setOnClickListener {
            familia.nombre = binding.etNombre.text.toString()
            familia.fecha_afiliacion =
                ArrayedDate.toArray(binding.tvFechaSeleccionada.text.toString())

            if(ArrayedDate.laterThanToday(ArrayedDate.toString(familia.fecha_afiliacion))) {
                shortToast("La fecha de afiliación no puede ser posterior a la actual")
                return@setOnClickListener
            }

            if(familia.nombre.isNullOrEmpty()) {
                shortToast("El nombre de la familia no puede quedar vacío")
                return@setOnClickListener
            }

            returnSimpleApiCall(
                { FamiliaApi().putFamilia(familia) },
                "Hubo un error. La familia no pudo ser modificado."
            )
        }
    }
}