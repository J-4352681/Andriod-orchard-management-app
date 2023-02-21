package app.lajusta.ui.ronda.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentRondaCreateBinding
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment

class RondaCreateFragment : BaseFragment() {
    private var _binding: FragmentRondaCreateBinding? = null
    private val binding get() = _binding!!
    private var ronda = Ronda(0, ArrayedDate.todayArrayed(), ArrayedDate.todayArrayed())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRondaCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fechaInicio = ronda.fecha_inicio.toMutableList()
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
                { RondaApi().postRonda(ronda) },
                "Hubo un error. La ronda no pudo ser creada."
            )
        }

        binding.bCancelar.setOnClickListener { activity!!.onBackPressed() }
    }



    private fun fillItem() {
        val fechaInicio = ronda.fecha_inicio.toMutableList()
        fechaInicio[1] += 1
        binding.tvFechaInicioSeleccionada.text = ArrayedDate.toString(fechaInicio)

        val fechaFin = ronda.fecha_fin!!.toMutableList()
        fechaFin[1] += 1
        binding.tvFechaFinSeleccionada.text = ArrayedDate.toString(fechaFin)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}