package app.lajusta.ui.verdura.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentVerduraModifyBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.verdura.api.VerduraApi

class VerduraModifyFragment: BaseFragment() {
    private var _binding: FragmentVerduraModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var verdura: Verdura

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
        _binding = FragmentVerduraModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()

        binding.etNombre.afterTextChanged { nombre -> verdura.nombre = nombre.trim() }
        binding.etDescripcion.afterTextChanged {
                descripcion -> verdura.descripcion = descripcion.trim()
        }
    }



    private fun setClickListeners() {
        binding.bFechaCosecha.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaCosechaSeleccionada
            ) { _, i, i2, i3 ->
                verdura.tiempo_cosecha = listOf(i, i2+1, i3)
                binding.tvFechaCosechaSeleccionada.text =
                    ArrayedDate.toString(verdura.tiempo_cosecha!!)
            }
        )

        binding.bFechaSiembra.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaSiembraSeleccionada
            ) { _, i, i2, i3 ->
                verdura.mes_siembra = listOf(i, i2+1, i3)
                binding.tvFechaSiembraSeleccionada.text =
                    ArrayedDate.toString(verdura.mes_siembra!!)
            }
        )

        binding.bGuardar.setOnClickListener {
            if(verdura.nombre.isEmpty()) {
                shortToast("La verdura debe tener nombre")
                return@setOnClickListener
            }

            returnSimpleApiCall(
                { VerduraApi().putVerdura(verdura) },
                "Hubo un error. La verdura no pudo ser creada."
            )
        }

        binding.bEliminar.setOnClickListener { returnSimpleApiCall(
            { VerduraApi().deleteVerdura(verdura.id_verdura) },
            "Hubo un error. La verdura no pudo ser eliminada."
        ) }
    }



    private fun fillItem() {
        binding.etNombre.setText(verdura.nombre)
        binding.etDescripcion.setText(verdura.descripcion)
        binding.tvFechaCosechaSeleccionada.text = ArrayedDate.toString(verdura.tiempo_cosecha!!)
        binding.tvFechaSiembraSeleccionada.text = ArrayedDate.toString(verdura.mes_siembra!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}