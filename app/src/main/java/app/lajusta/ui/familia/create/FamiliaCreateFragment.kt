package app.lajusta.ui.familia.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentFamiliaCreateBinding
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment

class FamiliaCreateFragment : BaseFragment() {
    private var _binding: FragmentFamiliaCreateBinding? = null
    private val binding get() = _binding!!
    private var familia = Familia(0, "", ArrayedDate.todayArrayed())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFamiliaCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var date = familia.fecha_afiliacion.toMutableList()
        date[1] += 1
        binding.tvFechaSeleccionada.text = ArrayedDate.toString(date)

        binding.bFecha.setOnClickListener(ArrayedDate.datePickerListener(
            activity!!, binding.tvFechaSeleccionada
        ) )

        binding.bGuardar.setOnClickListener {
            familia.nombre = binding.etFamilia.text.toString()
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
                { FamiliaApi().postFamilia(familia) },
                "Hubo un error. La familia no pudo ser creada."
            )
        }

        binding.bCancelar.setOnClickListener { activity!!.onBackPressed() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}