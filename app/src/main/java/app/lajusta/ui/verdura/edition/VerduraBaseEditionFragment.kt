package app.lajusta.ui.verdura.edition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentVerduraBaseEditionBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.verdura.Verdura

abstract class VerduraBaseEditionFragment : BaseFragment() {
    private var _binding: FragmentVerduraBaseEditionBinding? = null
    protected val binding get() = _binding!!
    protected var verdura = Verdura(
        0,
        ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 },
        ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 },
        "", "", ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            verdura = bundle.getParcelable("verdura")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerduraBaseEditionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startFragment()
    }

    private fun startFragment() {
        initNombre()
        initDescripcion()
        initDatePickerCosecha()
        initDatePickerSiembra()
        initSubmitAction()
        initDenyAction()
    }

    private fun initNombre() {
        binding.etNombre.setText(verdura.nombre)
        binding.etNombre.afterTextChanged { nombre -> verdura.nombre = nombre.trim() }
    }

    private fun initDescripcion() {
        binding.etDescripcion.setText(verdura.descripcion)
        binding.etDescripcion.afterTextChanged {
                descripcion -> verdura.descripcion = descripcion.trim()
        }
    }

    private fun initDatePickerCosecha() {
        binding.tvFechaCosechaSeleccionada.text = ArrayedDate.toString(verdura.tiempo_cosecha!!)

        binding.bFechaCosecha.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaCosechaSeleccionada
            ) { _, i, i2, i3 ->
                verdura.tiempo_cosecha = listOf(i, i2+1, i3)
                binding.tvFechaCosechaSeleccionada.text =
                    ArrayedDate.toString(verdura.tiempo_cosecha!!)
            }
        )
    }

    private fun initDatePickerSiembra() {
        binding.tvFechaSiembraSeleccionada.text = ArrayedDate.toString(verdura.mes_siembra!!)

        binding.bFechaSiembra.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaSiembraSeleccionada
            ) { _, i, i2, i3 ->
                verdura.mes_siembra = listOf(i, i2+1, i3)
                binding.tvFechaSiembraSeleccionada.text =
                    ArrayedDate.toString(verdura.mes_siembra!!)
            }
        )
    }

    private fun initSubmitAction() {
        binding.bSubmitAction.setOnClickListener {
            if(verdura.nombre.isEmpty()) {
                shortToast("La verdura debe tener nombre")
                return@setOnClickListener
            }

            commitChange()
        }
    }

    private fun initDenyAction() {
        binding.bDenyAction.setOnClickListener { denyAction() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun commitChange()

    abstract fun denyAction()
}