package app.lajusta.ui.visita.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentVisitaModifyBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.api.VisitaApi

class VisitaModifyFragment : BaseFragment() {
    private var _binding: FragmentVisitaModifyBinding? = null
    private val binding get() = _binding!!
    private var visita = Visita(
        0, ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 },
        "", -1, -1, listOf()
    )
    private lateinit var visitaArg: Visita

    private var quintas = listOf<Quinta>()
    private lateinit var quintasAdapter: ArrayAdapter<Quinta>
    private var tecnicos = listOf<Usuario>()
    private lateinit var usuariosAdapter: ArrayAdapter<Usuario>

    private val parcelas = mutableListOf<ParcelaVisita>()
    private lateinit var parcelasAdapter: ParcelaVisitaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            visitaArg = bundle.getParcelable("visita")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitaModifyBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startFragment()
    }

    private fun startFragment() {
        binding.etDesc.afterTextChanged { descripcion -> visita.descripcion = descripcion.trim() }

        binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)

        binding.bFecha.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaSeleccionada
            ) { _, i, i2, i3 ->
                visita.fecha_visita = listOf(i, i2+1, i3)
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
            }
        )

        apiCall(suspend {
            quintas = QuintaApi().getQuintas().body()!!
            tecnicos = UsuariosApi().getUsuarios().body()!!
        }, {
            initQuintaSpinner()
            initTecnicoSpinner()
            initRecyclerView()
            setClickListeners()
            fillFragmentWithItemData(visitaArg)
        }, "Hubo un error al actualizar la lista de visitas.")
    }

    private fun initQuintaSpinner() {
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
    }

    private fun initTecnicoSpinner() {
        usuariosAdapter = ArrayAdapter(activity!!, R.layout.spinner_item, tecnicos)
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
    }

    private fun setClickListeners() {
        val stateHandle: SavedStateHandle =
            findNavController().currentBackStackEntry?.savedStateHandle!!

        stateHandle.getLiveData<Visita>("visita")
            .observe(viewLifecycleOwner) { filledVisita ->
                fillFragmentWithItemData(filledVisita)
            }

        binding.btnAddParcela.setOnClickListener {
            val bundle = bundleOf("visita" to visita)
            findNavController().navigate(R.id.parcelaVisitaCreateFragment, bundle)
        }

        binding.bGuardar.setOnClickListener {
            if(visita.id_tecnico == -1) {
                shortToast("Debe seleccionar un tecnico")
                return@setOnClickListener
            }

            if(visita.id_quinta == -1) {
                shortToast("Debe escribir una quinta")
                return@setOnClickListener
            }

            returnSimpleApiCall(
                { VisitaApi().putVisita(visita) },
                "Hubo un error. La visita no pudo ser creada."
            )
        }

        binding.bBorrar.setOnClickListener {
            returnSimpleApiCall(
                { VisitaApi().deleteVisita(visita.id_visita) },
                "Hubo un error. No se pudo eliminar la visita."
            )
        }
    }

    private fun initRecyclerView() {
        parcelasAdapter = ParcelaVisitaAdapter(parcelas) { parcela ->
            visita.parcelas -= parcela
            parcelas.remove(parcelas.find { it.id_parcela == parcela.id_parcela })
            parcelasAdapter.notifyDataSetChanged()
        }
        binding.rvParcelas.layoutManager = LinearLayoutManager(activity)
        binding.rvParcelas.adapter = parcelasAdapter
    }

    private fun fillFragmentWithItemData(newVisita: Visita) {
        visita = newVisita
        parcelas.clear()
        parcelas.addAll(visita.parcelas)
        parcelasAdapter.notifyDataSetChanged()
        binding.etDesc.setText(visita.descripcion)
        binding.spinnerTecnico.setSelection(usuariosAdapter.getPosition(
            tecnicos.find { it.id_user == visita.id_tecnico }
        ))
        binding.spinnerQuinta.setSelection(quintasAdapter.getPosition(
            quintas.find { it.id_quinta == visita.id_quinta }
        ))
        binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
    }
}