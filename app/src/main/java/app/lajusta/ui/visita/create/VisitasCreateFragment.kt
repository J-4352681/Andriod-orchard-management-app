package app.lajusta.ui.visita.create

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import app.lajusta.R
import app.lajusta.databinding.FragmentVisitasCreateBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.parcela.Parcela
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.api.VisitaApi
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import kotlin.random.Random


class VisitasCreateFragment : BaseFragment() {

    private var _binding: FragmentVisitasCreateBinding? = null
    private val binding get() = _binding!!
    private var today = ArrayedDate.todayArrayed()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitasCreateBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    private val quintasList = mutableListOf<Quinta>()
    private val tecnicosList = mutableListOf<Usuario>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiCallGet()

        var date = today.toMutableList()
        date[1] += 1
        binding.tvFechaSeleccionada.text = ArrayedDate.toString(date)

        binding.bFecha.setOnClickListener(ArrayedDate.datePickerListener(
            activity!!, binding.tvFechaSeleccionada
        ) )

        binding.bGuardar.setOnClickListener {

            if(quintasList.isNullOrEmpty() && tecnicosList.isNullOrEmpty()) {
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

            if(fecha.isNullOrEmpty()) {
                shortToast("Debe seleccionar una fecha")
                return@setOnClickListener
            }

            if(tecnicoNom.isNullOrEmpty() && tecnicoId != null) {
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
        apiCall(suspend {
            val quintas = QuintaApi().getQuintas().body()!!
            val tecnicos = UsuariosApi().getUsuarios().body()!!

            activity!!.runOnUiThread {
                quintasList.clear()
                quintasList.addAll(quintas)

                tecnicosList.clear()
                tecnicosList.addAll(tecnicos)

                fillSpinners()
            }
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
            Verdura(1, arrayOf(2, 3, 4), arrayOf(1, 2, 4), "dsadfs", "Tomate", "fssd")
        val par = ParcelaVisita(1, 2, true, true, verdura)
        val par1 = ParcelaVisita(2, 3, false, true, verdura)
        return listOf(par, par1)
    }

    fun createId(): Int {
        /** Este ID sera reemplazado en el backend */
        return Random.nextInt(0, 1000)
    }

}