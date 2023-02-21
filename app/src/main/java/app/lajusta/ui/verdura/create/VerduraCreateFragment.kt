package app.lajusta.ui.verdura.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentVerduraCreateBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.verdura.api.VerduraApi

class VerduraCreateFragment : BaseFragment() {
    private var _binding: FragmentVerduraCreateBinding? = null
    private val binding get() = _binding!!
    private var verdura = Verdura(
        0, ArrayedDate.todayArrayed(),
        ArrayedDate.todayArrayed(), "", "", ""
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerduraCreateBinding.inflate(inflater, container, false)
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
                { VerduraApi().postVerdura(verdura) },
                "Hubo un error. La verdura no pudo ser creada."
            )
        }

        binding.bCancelar.setOnClickListener { activity!!.onBackPressed() }
    }



    private fun fillItem() {
        val fechaCosecha = verdura.tiempo_cosecha!!.toMutableList()
        fechaCosecha[1] += 1
        binding.tvFechaCosechaSeleccionada.text = ArrayedDate.toString(fechaCosecha)

        val fechaSiembra = verdura.mes_siembra!!.toMutableList()
        fechaSiembra[1] += 1
        binding.tvFechaSiembraSeleccionada.text = ArrayedDate.toString(fechaSiembra)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}