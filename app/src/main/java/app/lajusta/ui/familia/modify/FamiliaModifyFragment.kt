package app.lajusta.ui.familia.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentFamiliaModifyBinding
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.API.QuintaApi
import app.lajusta.ui.quinta.Quinta

class FamiliaModifyFragment: BaseFragment(){

    private var _binding: FragmentFamiliaModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var familia: Familia

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
        var date = familia.fecha_afiliacion.toMutableList()
        date[1] += 1
        binding.tvFechaSeleccionada.text = ArrayedDate.toString(date)

        binding.tvTitle.text =
            ("Familia " + familia.nombre
            + " afiliada " + ArrayedDate.toString(familia.fecha_afiliacion))

        binding.etNombre.setText(familia.nombre)

        apiCall(suspend {
            val quintas = QuintaApi().getQuintas().body()!!.filter { it.fpId == familia.id_fp }
            activity!!.runOnUiThread {
                val textQuintas = quintas.map { it.nombre }.toString()
                binding.tvQuintasList.text = textQuintas.subSequence(1, textQuintas.length-1)
            }
        }, "No se pudieron obtener las quintas de la familia.")

        /* apiCall(suspend {
            val bolsones = BolsonApi().getBolsones().body()!!
                .filter { it.idFp == familia.id_fp }
                .map { it. }
            val fechas =
            activity!!.runOnUiThread {
                val text
            }
        }) */ // TODO a completar
    }

    private fun setClickListeners() {
        binding.bFecha.setOnClickListener(ArrayedDate.datePickerListener(
            activity!!, binding.tvFechaSeleccionada
        ) )

        binding.bBorrar.setOnClickListener {
            simpleApiCall(
                { FamiliaApi().deleteFamilia(familia.id_fp) },
                "Hubo un error. El bolsón no pudo ser eliminado."
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

            simpleApiCall(
                { FamiliaApi().putFamilia(familia) },
                "Hubo un error. El familia no pudo ser modificado."
            )
        }
    }
}