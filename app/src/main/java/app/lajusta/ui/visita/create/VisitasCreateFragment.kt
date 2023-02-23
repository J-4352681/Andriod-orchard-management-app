package app.lajusta.ui.visita.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import app.lajusta.R
import app.lajusta.databinding.FragmentVisitasCreateBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.PrefilledVisita
import app.lajusta.ui.visita.api.VisitaApi


class VisitasCreateFragment : BaseFragment() {

    private var _binding: FragmentVisitasCreateBinding? = null
    private val binding get() = _binding!!
    private val visita = Visita(
        0, ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 },
        "", -1, -1, listOf()
    )
    private var prefilledVisita: PrefilledVisita? = null

    private var quintas = listOf<Quinta>()
    private lateinit var quintasAdapter: ArrayAdapter<Quinta>
    private var tecnicos = listOf<Usuario>()
    private lateinit var usuariosAdapter: ArrayAdapter<Usuario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            val data = bundle.getParcelable<PrefilledVisita>("prefilledVisita")
            if(data != null) prefilledVisita = data
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitasCreateBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
    }

    fun fillItem() {
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
            setClickListeners()
            prefillFields()
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
                { VisitaApi().postVisita(visita) },
                "Hubo un error. La visita no pudo ser creada."
            )

        }

        binding.bCancelar.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun prefillFields() {
        if(prefilledVisita != null) {
            if (!prefilledVisita!!.descripcion.isNullOrEmpty()) {
                // visita.descripcion = prefilledVisita?.descripcion
                binding.etDesc.setText(prefilledVisita?.descripcion)
                if (prefilledVisita!!._blocked) binding.etDesc.isEnabled = false
            }
            if (!prefilledVisita!!.fecha_visita.isNullOrEmpty()) {
                visita.fecha_visita = prefilledVisita?.fecha_visita!!
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
                if (prefilledVisita!!._blocked) binding.bFecha.isEnabled = false
            }
            if (prefilledVisita!!.id_quinta != null) {
                // visita.id_quinta = prefilledVisita?.quinta!!.id_quinta
                binding.spinnerQuinta.setSelection(
                    quintasAdapter.getPosition(quintas.find {
                        it.id_quinta == prefilledVisita?.id_quinta
                    })
                )
                if (prefilledVisita!!._blocked) binding.spinnerQuinta.isEnabled = false
            }
            if (prefilledVisita!!.id_tecnico != null) {
                // visita.id_tecnico = prefilledVisita?.tecnico!!.id_user
                binding.spinnerTecnico.setSelection(
                    usuariosAdapter.getPosition(tecnicos.find {
                        it.id_user == prefilledVisita?.id_tecnico
                    })
                )
                if (prefilledVisita!!._blocked) binding.spinnerTecnico.isEnabled = false
            }
            if (!prefilledVisita!!.parcelas.isNullOrEmpty()) {
                // visita.id_quinta = prefilledVisita?.quinta!!.id_quinta
                // TODO llenar parcelas
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}