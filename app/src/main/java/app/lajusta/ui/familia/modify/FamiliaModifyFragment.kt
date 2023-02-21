package app.lajusta.ui.familia.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import app.lajusta.R
import app.lajusta.databinding.FragmentFamiliaModifyBinding
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.familia.FamiliaCompleta
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.model.QuintaCompleta
import app.lajusta.ui.quinta.model.QuintaCompletaPrefill
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi

class FamiliaModifyFragment: BaseFragment(){

    private var _binding: FragmentFamiliaModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var familia: FamiliaCompleta
    private val bolsonesCompletos = mutableListOf<BolsonCompleto>()
    private val quintasCompletas = mutableListOf<QuintaCompleta>()
    private var rondas = listOf<Ronda>()

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
            setClickListeners()
        }, "No se pudieron obtener las quintas y/o bolsones de la familia.")
    }

    private fun fillQuintas() {
        quintasCompletas.clear()
        quintasCompletas.addAll(familia.quintas.map { quinta ->
            QuintaCompleta.toQuintaCompleta(quinta, familia.toFamilia())
        })

        val textQuintas = familia.quintas.map { it.nombre }.toString()
        binding.tvQuintasList.text = textQuintas.subSequence(1, textQuintas.length-1)
    }

    private fun fillRondas() {
        bolsonesCompletos.clear()
        bolsonesCompletos.addAll(familia.bolsones.map { bolson ->
            BolsonCompleto.toBolsonCompleto(bolson, familia.toFamilia(), familia.rondas.find {
                it.id_ronda == bolson.idRonda
            }!!)
        })

        val fechasBolsonesRondas = bolsonesCompletos.map {
            ArrayedDate.toString(it.ronda.fecha_inicio.toMutableList())
        }.toString()
        binding.tvUltimoBolson.text = fechasBolsonesRondas
            .subSequence(1, fechasBolsonesRondas.length-1)
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

        binding.bVerQuintas.setOnClickListener {
            val bundle = bundleOf("quintas" to ArrayList<QuintaCompleta>(quintasCompletas))
            this.findNavController().navigate(
                R.id.quintaFilteredListFragment, bundle
            )
        }

        binding.bNuevaQuinta.setOnClickListener {
            val bundle = bundleOf("quinta" to QuintaCompletaPrefill(
                null, null, null, familia.toFamilia()
            ))
            this.findNavController().navigate(
                R.id.quintaCreateFragment, bundle
            )
        }

        binding.bVerRondas.setOnClickListener {
            val bundle = bundleOf("rondas" to ArrayList<Ronda>(familia.rondas))
            this.findNavController().navigate(
                R.id.rondaFilteredListFragment, bundle
            )
        }
    }
}