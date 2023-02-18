package app.lajusta.ui.visita.modify

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentVisitaModifyBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.api.VisitaApi
import app.lajusta.ui.visita.model.VisitaCompleta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VisitaModifyFragment() : BaseFragment() {
    private var _binding: FragmentVisitaModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var visita: VisitaCompleta
    private lateinit var parcelaVisitaAdapter: ParcelaVisitaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            visita = bundle.getParcelable("visita")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitaModifyBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    private val quintasList = mutableListOf<Quinta>()
    private val tecnicosList = mutableListOf<Usuario>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()

        binding.bFecha.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaSeleccionada
            )
        )

        binding.bBorrar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    VisitaApi().deleteVisita(visita.id_visita)
                } catch (e: Exception) {
                    activity!!.runOnUiThread {
                        shortToast(
                            "Hubo un error. El elemento no pudo ser eliminado."
                        )
                    }
                } finally {
                    activity!!.runOnUiThread { activity!!.onBackPressed() }
                }
            }
        }

        binding.bGuardar.setOnClickListener {

            if (quintasList.isNullOrEmpty() && tecnicosList.isNullOrEmpty()) {
                shortToast("Hubo problemas recuperando los datos de la base de datos. Intente mas tarde.")
                return@setOnClickListener
            }

            val fecha = ArrayedDate.toArray(binding.tvFechaSeleccionada.text.toString())
            val tecnicoNom = binding.spinnerTecnico.selectedItem.toString().trim()
            val quintaNom = binding.spinnerQuinta.selectedItem.toString().trim()
            val quintaId = getQuintaIdByName(quintaNom)
            val tecnicoId = getTecnicoIdByName(tecnicoNom)

            val desc = binding.etDesc.text.toString().trim()
            val listaParcelas = parcelaVisitaAdapter.getParcelas()

            if (fecha.isNullOrEmpty()) {
                shortToast("Debe seleccionar una fecha")
                return@setOnClickListener
            }

            if (tecnicoNom.isNullOrEmpty() && tecnicoId != null) {
                shortToast("Debe seleccionar un tecnico")
                return@setOnClickListener
            }

            if (quintaNom.isNullOrEmpty() && quintaId != null) {
                shortToast("Debe escribir una quinta")
                return@setOnClickListener
            }

            try {
                visita.fecha_visita = fecha
                visita.descripcion = desc
                visita.tecnico.id_user = tecnicoId!!
                visita.quinta.id_quinta = quintaId!!
                visita.parcelas = listaParcelas

                returnSimpleApiCall({
                    VisitaApi().putVisita(visita.toVisita())
                }, "Hubo un error. La visita no pudo ser creada.")

            } catch (e: Exception) {
                shortToast("Complete los campos con valores validos según corresponda.")
            }
        }
    }

    private fun fillItem() {

        apiCallGet(visita.tecnico.nombre, visita.quinta.nombre) //llena las comboBox

        binding.tvTitle.text = "Modificando visita " + visita.id_visita.toString()
        binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
        binding.etDesc.setText(visita.descripcion.toString())



        initRecyclerView()
    }

    fun apiCallGet(startingTec: String, staringQuinta: String) {
        apiCall(suspend {
            val quintas = QuintaApi().getQuintas().body()!!
            val tecnicos = UsuariosApi().getUsuarios().body()!!

            activity!!.runOnUiThread {
                quintasList.clear()
                quintasList.addAll(quintas)

                tecnicosList.clear()
                tecnicosList.addAll(tecnicos)

                fillSpinners(startingTec, staringQuinta)
            }
        }, "Hubo un error al buscar los datos de quintas y tecnicos.")
    }

    private fun fillSpinners(startingTec: String, staringQuinta: String) {
        // access the items of the list
        val nombresQuintas: Array<String> = getNombreQuintas(quintasList)
        val nombresTecnicos: Array<String> = getNombreTecnicos(tecnicosList)

        // access the spinner
        val spinnerQuintas = binding.spinnerQuinta
        val adapterQuintas: ArrayAdapter<String>? = (activity?.let {
            ArrayAdapter<String>(
                it,
                app.lajusta.R.layout.spinner_item, nombresQuintas
            )
        })

        val spinnerTecnicos = binding.spinnerTecnico
        val adapterTecnicos: ArrayAdapter<String>? = (activity?.let {
            ArrayAdapter<String>(
                it,
                app.lajusta.R.layout.spinner_item, nombresTecnicos
            )
        })

        // bind
        spinnerQuintas.adapter = adapterQuintas
        spinnerTecnicos.adapter = adapterTecnicos


        // on below line we are getting the position of the item by the item name in our adapter.
        val spinnerQuintaPosition: Int = adapterQuintas!!.getPosition(staringQuinta)
        val spinnerTecnicoPosition: Int = adapterTecnicos!!.getPosition(startingTec)

        // on below line we are setting selection for our spinner to spinner position.
        spinnerQuintas.setSelection(spinnerQuintaPosition)
        spinnerTecnicos.setSelection(spinnerTecnicoPosition)
    }

    private fun getNombreQuintas(quintas: List<Quinta>): Array<String> {
        return quintas.map { it.nombre.orEmpty() }.toTypedArray()
    }

    private fun getNombreTecnicos(tecnicos: List<Usuario>): Array<String> {
        return tecnicos.map { it.nombre.orEmpty() }.toTypedArray()
    }

    private fun getQuintaByName(name: String): Quinta? {
        return quintasList?.find { it.nombre == name }
    }

    private fun getQuintaIdByName(name: String): Int? {
        return getQuintaByName(name)?.id_quinta
    }

    private fun getTecnicoByName(name: String): Usuario? {
        return tecnicosList?.find { it.nombre == name }
    }

    private fun getTecnicoIdByName(name: String): Int? {
        return getTecnicoByName(name)?.id_user
    }

    private fun initRecyclerView() {
        parcelaVisitaAdapter = ParcelaVisitaAdapter(visita.parcelas)
        binding.rvParcelas.layoutManager = LinearLayoutManager(activity)
        binding.rvParcelas.adapter = parcelaVisitaAdapter
    }
}