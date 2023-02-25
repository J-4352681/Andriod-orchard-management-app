package app.lajusta.ui.ronda.edition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentRondaBaseEditionBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.ronda.Ronda

abstract class RondaBaseEditionFragment: BaseFragment() {
    private var _binding: FragmentRondaBaseEditionBinding? = null
    protected val binding get() = _binding!!
    protected var ronda = Ronda(0,
        ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 },
        ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 }
    )

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
    ): View {
        _binding = FragmentRondaBaseEditionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()
    }

    private fun fillItem() {
        binding.tvFechaInicioSeleccionada.text = ArrayedDate.toString(ronda.fecha_inicio)
        binding.tvFechaFinSeleccionada.text = ArrayedDate.toString(ronda.fecha_fin!!)
    }

    private fun setClickListeners() {
        binding.bFechaInicio.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaInicioSeleccionada
            ) { _, i, i2, i3 ->
                ronda.fecha_inicio = listOf(i, i2+1, i3)
                binding.tvFechaInicioSeleccionada.text = ArrayedDate.toString(ronda.fecha_inicio)
            }
        )

        binding.bFechaFin.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaFinSeleccionada
            ) { _, i, i2, i3 ->
                ronda.fecha_fin = listOf(i, i2+1, i3)
                binding.tvFechaFinSeleccionada.text = ArrayedDate.toString(ronda.fecha_fin!!)
            }
        )

        binding.bSubmitAction.setOnClickListener { commitChange() }

        binding.bDenyAction.setOnClickListener { denyAction() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun commitChange()

    abstract fun denyAction()
}