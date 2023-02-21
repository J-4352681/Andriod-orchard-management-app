package app.lajusta.ui.ronda.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentRondaModifyBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi

class RondaModifyFragment: BaseFragment() {
    private var _binding: FragmentRondaModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var ronda: Ronda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            ronda = bundle.getParcelable("ronda")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRondaModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()
    }



    private fun setClickListeners() {
        binding.bFechaInicio.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaInicioSeleccionada
            ) )

        binding.bFechaFin.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaFinSeleccionada
            ) )

        binding.bGuardar.setOnClickListener {
            ronda.fecha_inicio =
                ArrayedDate.toArray(binding.tvFechaInicioSeleccionada.text.toString())

            ronda.fecha_fin =
                ArrayedDate.toArray(binding.tvFechaFinSeleccionada.text.toString())

            returnSimpleApiCall(
                { RondaApi().putRonda(ronda) },
                "Hubo un error. La ronda no pudo ser creada."
            )
        }

        binding.bEliminar.setOnClickListener { returnSimpleApiCall(
            { RondaApi().deleteRonda(ronda.id_ronda) },
            "Hubo un error. La ronda no pudo ser eliminada."
        ) }
    }



    private fun fillItem() {
        binding.tvFechaInicioSeleccionada.text = ArrayedDate.toString(ronda.fecha_inicio)
        binding.tvFechaFinSeleccionada.text = ArrayedDate.toString(ronda.fecha_fin!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}