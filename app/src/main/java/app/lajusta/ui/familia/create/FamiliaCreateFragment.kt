package app.lajusta.ui.familia.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentFamiliaCreateBinding
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.PrefilledFamilia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged

class FamiliaCreateFragment : BaseFragment() {
    private var _binding: FragmentFamiliaCreateBinding? = null
    private val binding get() = _binding!!
    private var familia = Familia(
        0, "",
        ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 }
    )
    private var prefilledFamilia: PrefilledFamilia? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            if(bundle.containsKey("prefilledFamilia"))
                prefilledFamilia = bundle.getParcelable("prefilledFamilia")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamiliaCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startFragment()
    }

    private fun startFragment() {
        initFecha()
        initNombre()
        initGuardar()
        initCancelar()
        prefillFamilia()
    }

    private fun initFecha() {
        binding.tvFechaSeleccionada.text = ArrayedDate.toString(familia.fecha_afiliacion)

        binding.bFecha.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaSeleccionada
            ) { _, i, i2, i3 ->
                familia.fecha_afiliacion = listOf(i, i2+1, i3)
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(familia.fecha_afiliacion)
            }
        )
    }

    private fun initNombre() {
        binding.etFamilia.afterTextChanged { nombre -> familia.nombre = nombre }
    }

    private fun initGuardar() {
        binding.bGuardar.setOnClickListener {
            if(ArrayedDate.laterThanToday(ArrayedDate.toString(familia.fecha_afiliacion))) {
                shortToast("La fecha de afiliación no puede ser posterior a la actual")
                return@setOnClickListener
            }

            if(familia.nombre.isEmpty()) {
                shortToast("El nombre de la familia no puede quedar vacío")
                return@setOnClickListener
            }

            returnSimpleApiCall(
                { FamiliaApi().postFamilia(familia) },
                "Hubo un error. La familia no pudo ser creada."
            )
        }
    }

    private fun initCancelar() =
        binding.bCancelar.setOnClickListener { activity!!.onBackPressed() }

    private fun prefillFamilia() {
        if(prefilledFamilia != null) {
            if(prefilledFamilia?.nombre != null) {
                familia.nombre = prefilledFamilia?.nombre!!
                binding.etFamilia.setText(familia.nombre)
                if(prefilledFamilia?._blockFields!!) binding.etFamilia.isEnabled = false
            }
            if(prefilledFamilia?.fecha_afiliacion != null) {
                familia.fecha_afiliacion = prefilledFamilia?.fecha_afiliacion!!
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(familia.fecha_afiliacion)
                if(prefilledFamilia?._blockFields!!) binding.bFecha.isEnabled = false
            }
            if(prefilledFamilia?._blockSubmitAction!!) binding.bGuardar.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}