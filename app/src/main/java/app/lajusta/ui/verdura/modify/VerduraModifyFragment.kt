package app.lajusta.ui.verdura.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentVerduraModifyBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
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
    ): View? {
        _binding = FragmentVerduraModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()
    }



    private fun setClickListeners() {
        binding.bFechaCosecha.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaCosechaSeleccionada
            ) )

        binding.bFechaSiembra.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaSiembraSeleccionada
            ) )

        binding.bGuardar.setOnClickListener {
            if(binding.etNombre.text.isEmpty()) {
                shortToast("La verdura debe tener nombre")
                return@setOnClickListener
            }

            verdura.nombre = binding.etNombre.text.toString()
            verdura.descripcion = binding.etDescripcion.text.toString()

            verdura.tiempo_cosecha =
                ArrayedDate.toArray(binding.tvFechaCosechaSeleccionada.text.toString())

            verdura.mes_siembra =
                ArrayedDate.toArray(binding.tvFechaSiembraSeleccionada.text.toString())

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