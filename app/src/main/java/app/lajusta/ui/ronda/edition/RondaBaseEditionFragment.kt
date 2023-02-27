package app.lajusta.ui.ronda.edition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentRondaBaseEditionBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.ronda.PrefilledRonda
import app.lajusta.ui.ronda.Ronda

abstract class RondaBaseEditionFragment: BaseFragment() {
    private var _binding: FragmentRondaBaseEditionBinding? = null
    protected val binding get() = _binding!!
    protected var ronda = Ronda(0,
        ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 },
        ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 }
    )
    private var prefilledRonda: PrefilledRonda? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            if (bundle.containsKey("ronda"))
                ronda = bundle.getParcelable("ronda")!!
            if (bundle.containsKey("prefilledRonda"))
                prefilledRonda = bundle.getParcelable("prefilledRonda")!!
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
        prefillFields()
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

    private fun prefillFields() {
        if(prefilledRonda != null) {
            if(!prefilledRonda?.fecha_fin.isNullOrEmpty()) {
                binding.tvFechaFinSeleccionada.text =
                    ArrayedDate.toString(prefilledRonda?.fecha_fin!!)
                if (prefilledRonda!!._blockFields) binding.bFechaFin.isEnabled = false
            }
            if(!prefilledRonda?.fecha_inicio.isNullOrEmpty()) {
                binding.tvFechaInicioSeleccionada.text =
                    ArrayedDate.toString(prefilledRonda?.fecha_inicio!!)
                if (prefilledRonda!!._blockFields) binding.bFechaInicio.isEnabled = false
            }
            if(prefilledRonda!!._blockSubmitAction) {
                binding.bSubmitAction.isEnabled = false
                binding.bDenyAction.isEnabled = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun commitChange()

    abstract fun denyAction()
}