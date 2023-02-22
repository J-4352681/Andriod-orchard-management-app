package app.lajusta.ui.visita.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import app.lajusta.R
import app.lajusta.databinding.FragmentVisitasCreateBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.api.VisitaApi
import kotlin.random.Random


class VisitasCreateFragment : BaseFragment() {

    private var _binding: FragmentVisitasCreateBinding? = null
    private val binding get() = _binding!!
    private val visita = Visita(
        0, ArrayedDate.todayArrayed().toMutableList().also { it[1]+=1 },
        "", -1, -1, listOf()
    )
    private val quintasList = mutableListOf<Quinta>()
    private val tecnicosList = mutableListOf<Usuario>()

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
        apiCallGet()

        binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)

        binding.bFecha.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaSeleccionada
            ) { _, i, i2, i3 ->
                visita.fecha_visita = listOf(i, i2+1, i3)
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
            }
        )

        binding.bGuardar.setOnClickListener {

            if(quintasList.isEmpty() && tecnicosList.isEmpty()) {
                shortToast("Hubo problemas recuperando los datos de la base de datos. Intente mas tarde.")
                return@setOnClickListener
            }

            val fecha = ArrayedDate.toArray(binding.tvFechaSeleccionada.text.toString())
            val tecnicoNom = binding.spinnerTecnico.selectedItem.toString().trim()
            val quintaNom = binding.spinnerQuinta.selectedItem.toString().trim()
            val quintaId = getQuintaIdByName(quintaNom)
            val tecnicoId = getTecnicoIdByName(tecnicoNom)

            val desc = binding.etDesc.text.toString().trim()
            val listaParcelas = listOf<ParcelaVisita>() /** CAMBIAR */

            if(fecha.isEmpty()) {
                shortToast("Debe seleccionar una fecha")
                return@setOnClickListener
            }

            if(tecnicoNom.isEmpty() && tecnicoId != null) {
                shortToast("Debe seleccionar un tecnico")
                return@setOnClickListener
            }

            if(quintaNom.isNullOrEmpty() && quintaId != null) {
                shortToast("Debe escribir una quinta")
                return@setOnClickListener
            }


            returnSimpleApiCall({
                VisitaApi().postVisita(
                    Visita(0,fecha,desc,tecnicoId!!,quintaId!!,listaParcelas)
                )
            }, "Hubo un error. La visita no pudo ser creada.")

        }

        binding.bCancelar.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun apiCallGet() {
        lateinit var quintas: List<Quinta>
        lateinit var tecnicos: List<Usuario>

        apiCall(suspend {
            quintas = QuintaApi().getQuintas().body()!!
            tecnicos = UsuariosApi().getUsuarios().body()!!
        }, {
            quintasList.clear()
            quintasList.addAll(quintas)

            tecnicosList.clear()
            tecnicosList.addAll(tecnicos)

            fillSpinners()
        }, "Hubo un error al actualizar la lista de visitas.")
    }

    private fun fillSpinners(){
        // access the items of the list
        val nombresQuintas: Array<String> = getNombreQuintas(quintasList)
        val nombresTecnicos: Array<String> = getNombreTecnicos(tecnicosList)

        // access the spinner
        val spinnerQuintas = binding.spinnerQuinta
        val adapterQuintas: ArrayAdapter<String>? = (activity?.let {
            ArrayAdapter<String>(
                it,
                R.layout.spinner_item, nombresQuintas
            )
        })

        val spinnerTecnicos = binding.spinnerTecnico
        val adapterTecnicos: ArrayAdapter<String>? = (activity?.let {
            ArrayAdapter<String>(
                it,
                R.layout.spinner_item, nombresTecnicos
            )
        })

        // bind
        spinnerQuintas.adapter = adapterQuintas
        spinnerTecnicos.adapter = adapterTecnicos
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