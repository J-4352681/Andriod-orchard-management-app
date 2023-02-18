package app.lajusta.ui.familia.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import app.lajusta.R
import app.lajusta.databinding.FragmentFamiliaModifyBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.FamiliaCompleta
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.rondas.Ronda
import app.lajusta.ui.rondas.api.RondaApi
import kotlinx.coroutines.*

class FamiliaModifyFragment: BaseFragment(){

    private var _binding: FragmentFamiliaModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var familia: FamiliaCompleta
    private val bolsonesCompletos = mutableListOf<BolsonCompleto>()
    private var rondas = listOf<Ronda>()
    private var bolsones = mutableListOf<Bolson>()

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
            rondas = RondaApi().getRondas().body()!!.toMutableList()
        }, {
            fillQuintas()
            fillRondas()
        }, "No se pudieron obtener las quintas y/o bolsones de la familia.")
    }

    private fun fillQuintas() {
        val textQuintas = familia.quintas.map { it.nombre }.toString()
        binding.tvQuintasList.text = textQuintas.subSequence(1, textQuintas.length-1)
    }

    private fun fillRondas() {
        rondas
    }

    /* private fun fillRondas2() {
        bolsones = bolsones.filter { it.idFp == familia.id_fp }.toMutableList()
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
        binding.tvUltimoBolson.text = fechasBolsonesRondas
            .subSequence(1, fechasBolsonesRondas.length-1)
    } */

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
                { FamiliaApi().putFamilia(familia.toFamilia()) },
                "Hubo un error. La familia no pudo ser modificado."
            )
        }

        binding.bVerBolsones.setOnClickListener {
            val bundle = bundleOf("bolsones" to ArrayList<BolsonCompleto>(bolsonesCompletos))
            this.findNavController().navigate(
                R.id.bolsonFilteredListFragment, bundle
            )
        }
    }
}