package app.lajusta.ui.visita.parcela

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.navigation.fragment.findNavController
import app.lajusta.R
import app.lajusta.databinding.FragmentParcelaVisitaCreateBinding
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.verdura.api.VerduraApi
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.model.VisitaCompleta
import kotlinx.coroutines.*

class ParcelaVisitaCreateFragment : BaseFragment() {
    private var _binding: FragmentParcelaVisitaCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var visita: Visita
    private val parcela = ParcelaVisita(0, 0,
        false, false, -1,
        Verdura(0, listOf(), listOf(), "", "", "")
    )

    private var verduras = listOf<Verdura>()
    private lateinit var verdurasAdapter: ArrayAdapter<Verdura>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            visita = bundle.getParcelable("visita")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParcelaVisitaCreateBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()
    }

    private fun fillItem() {
        binding.etSurcos.afterTextChanged { cantidad ->
            parcela.cantidad_surcos = if (cantidad != "") cantidad.toInt() else 0
        }

        parcela.cosecha = binding.cbCosecha.isChecked
        binding.cbCosecha.setOnClickListener { parcela.cosecha = !parcela.cosecha }

        parcela.cubierta = binding.cbCubierto.isChecked
        binding.cbCubierto.setOnClickListener { parcela.cubierta = !parcela.cubierta }

        apiCall(
            { verduras = VerduraApi().getVerduras().body()!! },
            {
                initSpinnerVerdura()
                setClickListeners()
            },
            "Hubo un problema al obtener las verduras."
        )
    }

    private fun initSpinnerVerdura() {
        verdurasAdapter = ArrayAdapter(activity!!, R.layout.spinner_item, verduras)
        binding.sVerdura.adapter = verdurasAdapter
        binding.sVerdura.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, p1: View?, position: Int, p3: Long
                ) {
                    val verduraSeleccionada = binding.sVerdura.selectedItem as Verdura
                    parcela.verdura = verduraSeleccionada
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun setClickListeners() {
        binding.bGuardar.setOnClickListener {
            visita.parcelas += parcela
            val navController = this.findNavController()
            navController.previousBackStackEntry?.savedStateHandle!!["visita"] = visita
            navController.popBackStack()
        }

        binding.bCancelar.setOnClickListener {
            val navController = this.findNavController()
            navController.previousBackStackEntry?.savedStateHandle!!["visita"] = visita
            navController.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}