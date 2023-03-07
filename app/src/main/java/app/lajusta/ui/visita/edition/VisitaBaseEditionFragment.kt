package app.lajusta.ui.visita.edition

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.data.Preferences.PreferenceHelper.userId
import app.lajusta.data.Preferences.PreferenceHelper.username
import app.lajusta.databinding.FragmentVisitaBaseEditionBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi
import app.lajusta.ui.visita.PrefilledVisita
import app.lajusta.ui.visita.Visita

abstract class VisitaBaseEditionFragment : BaseFragment() {
    private var _binding: FragmentVisitaBaseEditionBinding? = null
    protected val binding get() = _binding!!
    protected var visita = Visita(
        0, ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 },
        "", -1, -1, mutableListOf()
    )
    private var prefilledVisita: PrefilledVisita? = null

    protected var quintas = listOf<Quinta>()
    private lateinit var quintasAdapter: ArrayAdapter<Quinta>
    private var tecnicos = listOf<Usuario>()
    private lateinit var usuariosAdapter: ArrayAdapter<Usuario>

    private lateinit var parcelasAdapter: ParcelaVisitaAdapter

    val CUSTOM_PREF_NAME = "User_data"
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            if (bundle.containsKey("visita"))
                visita = bundle.getParcelable("visita")!!
            if (bundle.containsKey("prefilledVisita"))
                prefilledVisita = bundle.getParcelable("prefilledVisita")
        }
        prefs = activity!!.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitaBaseEditionBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startFragment()
    }

    private fun startFragment() {
        initDescripcion()
        initDateSelector()
        apiCall(suspend {
            quintas = QuintaApi().getQuintas().body()!!
            tecnicos = UsuariosApi().getUsuarios().body()!!
        }, {
            initQuintaSpinner()
            //initTecnicoSpinner()
            setClickListeners()
            prefillFields()
        }, "Hubo un error al actualizar la lista de visitas.")
        initRecyclerView()
    }

    private fun initDescripcion() {
        binding.etDesc.setText(visita.descripcion)
        binding.etDesc.afterTextChanged { descripcion -> visita.descripcion = descripcion.trim() }
    }

    private fun initDateSelector() {
        binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
        binding.bFecha.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaSeleccionada
            ) { _, i, i2, i3 ->
                visita.fecha_visita = listOf(i, i2+1, i3)
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
            }
        )
    }

    private fun initQuintaSpinner() {
        val idQuintaSeleccionada = visita.id_quinta
        quintasAdapter = ArrayAdapter(activity!!, R.layout.spinner_item, quintas)
        binding.spinnerQuinta.adapter = quintasAdapter
        binding.spinnerQuinta.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, p1: View?, position: Int, p3: Long
                ) {
                    val quintaSeleccionada = binding.spinnerQuinta.selectedItem as Quinta
                    visita.id_quinta = quintaSeleccionada.id_quinta
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        binding.spinnerQuinta.setSelection(
            quintasAdapter.getPosition(quintas.find { it.id_quinta == idQuintaSeleccionada })
        )
    }

    /*private fun initTecnicoSpinner() {
        //val idTecnicoSeleccionado = visita.id_tecnico
        /*usuariosAdapter = ArrayAdapter(activity!!, R.layout.spinner_item, tecnicos)
        binding.spinnerTecnico.adapter = usuariosAdapter
        binding.spinnerTecnico.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, p1: View?, position: Int, p3: Long
                ) {
                    val tecnicoSeleccionado = binding.spinnerTecnico.selectedItem as Usuario
                    visita.id_tecnico = tecnicoSeleccionado.id_user
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        binding.spinnerTecnico.setSelection(
            usuariosAdapter.getPosition(tecnicos.find { it.id_user == idTecnicoSeleccionado })
        )*/

    }*/

    private fun setClickListeners() {
        findNavController().currentBackStackEntry?.savedStateHandle!!
            .getLiveData<Visita>("visita").observe(viewLifecycleOwner) { visita = it }

        binding.btnAddParcela.setOnClickListener {
            val bundle = bundleOf("visita" to visita)
            findNavController().navigate(R.id.parcelaVisitaCreateFragment, bundle)
        }

        binding.bDenyAction.setOnClickListener { denyAction() }

        binding.bSubmitAction.setOnClickListener {
            if(visita.id_tecnico == -1) {
                shortToast("Debe seleccionar un tecnico")
                return@setOnClickListener
            }

            if(visita.id_quinta == -1) {
                shortToast("Debe escribir una quinta")
                return@setOnClickListener
            }

            commitChange()
        }
    }

    private fun initRecyclerView() {
        parcelasAdapter = ParcelaVisitaAdapter(visita.parcelas)
        binding.rvParcelas.layoutManager = LinearLayoutManager(activity)
        binding.rvParcelas.adapter = parcelasAdapter
    }

    private fun prefillFields() {
        if(prefilledVisita != null) {
            if (!prefilledVisita!!.descripcion.isNullOrEmpty()) {
                binding.etDesc.setText(prefilledVisita?.descripcion)
                if (prefilledVisita!!._blockFields) binding.etDesc.isEnabled = false
            }
            if (!prefilledVisita!!.fecha_visita.isNullOrEmpty()) {
                visita.fecha_visita = prefilledVisita?.fecha_visita!!
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
                if (prefilledVisita!!._blockFields) binding.bFecha.isEnabled = false
            }
            if (prefilledVisita!!.id_quinta != null) {
                visita.id_quinta = prefilledVisita?.id_quinta!!
                binding.spinnerQuinta.setSelection(
                    quintasAdapter.getPosition(quintas.find {
                        it.id_quinta == prefilledVisita?.id_quinta
                    })
                )
                if (prefilledVisita!!._blockFields) binding.spinnerQuinta.isEnabled = false
            }
            if (prefilledVisita!!.id_tecnico != null) {
                visita.id_tecnico = prefilledVisita?.id_tecnico!!
                /*binding.spinnerTecnico.setSelection(
                    usuariosAdapter.getPosition(tecnicos.find {
                        it.id_user == prefilledVisita?.id_tecnico
                    })
                )
                if (prefilledVisita!!._blockFields) binding.spinnerTecnico.isEnabled = false*/
                binding.tvTecnName.text = tecnicos.find { it.id_user == visita.id_tecnico }!!.nombre
            }
            if (!prefilledVisita!!.parcelas.isNullOrEmpty()) {
                visita.parcelas.clear()
                visita.parcelas.addAll(prefilledVisita!!.parcelas!!)
                parcelasAdapter.notifyDataSetChanged()
            }
            if(prefilledVisita!!._blockSubmitAction) {
                binding.bSubmitAction.isEnabled = false
                binding.bDenyAction.isEnabled = false
            }
        } else {
            visita.id_tecnico = prefs.userId
            binding.tvTecnName.text = prefs.username
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected abstract fun commitChange()

    protected abstract fun denyAction()
}