package app.lajusta.ui.visita.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.MainActivity.Companion.userId
import app.lajusta.databinding.FragmentVisitasForQuintaCreateBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.quinta.model.QuintaCompleta
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.api.VisitaApi
import app.lajusta.ui.visita.modify.ParcelaVisitaAdapter
import kotlin.random.Random


class VisitasForQuintaCreateFragment : BaseFragment() {
    private var _binding: FragmentVisitasForQuintaCreateBinding? = null
    private val binding get() = _binding!!
    private var hayVisitaPrevia = false
    private lateinit var visita: Visita
    private lateinit var quinta: QuintaCompleta
    private lateinit var parcelaVisitaAdapter: ParcelaVisitaAdapter
    private val tecnicosList = mutableListOf<Usuario>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            quinta = bundle.getParcelable("quinta")!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitasForQuintaCreateBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = "Creando visita a la quinta " + quinta.nombre
        apiCallGet() //llena las comboBox
        getLastVisita()



        binding.bFecha.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaSeleccionada
            ) { _, i, i2, i3 ->
                visita.fecha_visita = listOf(i, i2+1, i3)
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
            }
        )

        binding.bGuardar.setOnClickListener {

            if (tecnicosList.isNullOrEmpty()) {
                shortToast("Hubo problemas recuperando los datos de la base de datos. Intente mas tarde.")
                return@setOnClickListener
            }

            val fecha = ArrayedDate.toArray(binding.tvFechaSeleccionada.text.toString())
            val tecnicoNom = binding.spinnerTecnico.selectedItem.toString().trim()
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

            try {

                val v = Visita(0, fecha, desc, tecnicoId!!, quinta.id_quinta, listaParcelas)

                returnSimpleApiCall({
                    VisitaApi().putVisita(v)
                }, "Hubo un error. La visita no pudo ser creada.")

            } catch (e: Exception) {
                shortToast("Complete los campos con valores validos según corresponda.")
            }
        }

        binding.bCancelar.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun getLastVisita() {

        var visitas = listOf<Visita>()
        apiCall(suspend {
            visitas = VisitaApi().getVisitas().body()!!
        }, {

            var visitasDeLaQuinta = visitas.filter { it.id_quinta == quinta.id_quinta }
            if (visitasDeLaQuinta.isNotEmpty()) {
                visita = visitasDeLaQuinta.max() //Compara las fechas definido en Visitas
                fillItemWithVisita()
            } else {
                fillItemWithoutVisita()
            }


        }, "Hubo un error al buscar la informacion de la previa visita.")

    }

    private fun fillItemWithVisita() {

        binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
        binding.etDesc.setText(visita.descripcion.toString())
        parcelaVisitaAdapter = ParcelaVisitaAdapter(visita.parcelas)

        initRecyclerView()
    }

    private fun fillItemWithoutVisita() {

        var today = ArrayedDate.todayArrayed()
        var date = today.toMutableList()
        date[1] += 1
        binding.tvFechaSeleccionada.text = ArrayedDate.toString(date)

        parcelaVisitaAdapter = ParcelaVisitaAdapter(listOf())

        initRecyclerView()
    }

    fun apiCallGet() {
        lateinit var tecnicos: List<Usuario>

        apiCall(suspend {
            tecnicos = UsuariosApi().getUsuarios().body()!!
        }, {
            tecnicosList.clear()
            tecnicosList.addAll(tecnicos)

            fillSpinners()
        }, "Hubo un error al buscar los datos de quintas y tecnicos.")
    }


    private fun fillSpinners() {
        val nombresTecnicos: Array<String> = getNombreTecnicos(tecnicosList)

        val spinnerTecnicos = binding.spinnerTecnico
        val adapterTecnicos: ArrayAdapter<String>? = (activity?.let {
            ArrayAdapter<String>(
                it,
                app.lajusta.R.layout.spinner_item, nombresTecnicos
            )
        })

        spinnerTecnicos.adapter = adapterTecnicos


        // on below line we are getting the position of the item by the item name in our adapter.
        val spinnerTecnicoPosition: Int =
            adapterTecnicos!!.getPosition(getTecnicoNameById(userId!!))
        spinnerTecnicos.setSelection(spinnerTecnicoPosition)

    }

    private fun initRecyclerView() {
        binding.rvParcelas.layoutManager = LinearLayoutManager(activity)
        binding.rvParcelas.adapter = parcelaVisitaAdapter
    }



    private fun getNombreTecnicos(tecnicos: List<Usuario>): Array<String> {
        return tecnicos.map { it.nombre.orEmpty() }.toTypedArray()
    }

    private fun getTecnicoByName(name: String): Usuario? {
        return tecnicosList?.find { it.nombre == name }
    }

    private fun getTecnicoIdByName(name: String): Int? {
        return getTecnicoByName(name)?.id_user
    }

    private fun getTecnicoNameById(id: Int): String? {
        return tecnicosList?.find { it.id_user == id }?.nombre
    }

    fun getListaDeParcelasVisita(): List<ParcelaVisita> {
        /** Funcionalidad provisoria */
        val verdura: Verdura =
            Verdura(1, listOf(2, 3, 4), listOf(1, 2, 4), "dsadfs", "Tomate", "fssd")
        val par = ParcelaVisita(1, 2, true, true, verdura)
        val par1 = ParcelaVisita(2, 3, false, true, verdura)
        return listOf(par, par1)
    }

    fun createId(): Int {
        /** Este ID sera reemplazado en el backend */
        return Random.nextInt(0, 1000)
    }

}