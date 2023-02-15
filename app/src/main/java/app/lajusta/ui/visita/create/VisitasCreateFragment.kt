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
import app.lajusta.ui.parcela.Parcela
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.verdura.Verdura
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


class VisitasCreateFragment : Fragment(R.layout.fragment_visitas_create) {

    private var _binding:FragmentVisitasCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitasCreateBinding.inflate(
            inflater,
            container,
            false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // access the items of the list
        val nombresQuintas: Array<String> = getNombreQuintas()

        // access the spinner
        val spinner = binding.spinnerQuinta
        val adapter: ArrayAdapter<String>? = (activity?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_item, nombresQuintas
            )
        })

        binding.spinnerQuinta.adapter = adapter


        binding.bGuardar.setOnClickListener{

            val fecha = binding.etFecha.text.toString().trim()
            val tecnicoId = binding.etTecnico.text.toString().trim()
            val quintaNom = binding.spinnerQuinta.selectedItem.toString().trim()

            if ( fecha.isNotEmpty() && tecnicoId.isNotEmpty() && quintaNom.isNotEmpty() ){

                Toast.makeText(activity, "Datos correctos. Intento de postear.", Toast.LENGTH_SHORT).show()


                val idVisita = createId()
                val desc = binding.etDesc.text.toString().trim()
                val quintaId = getQuintaIdByName(quintaNom)
                val listaParcelas = getListaDeParcelas(idVisita)

                // Create JSON using JSONObject
                val jsonObject = JSONObject()
                jsonObject.put("id_visita", idVisita)
                jsonObject.put("fecha_visita", fecha)
                jsonObject.put("descripcion", desc)
                jsonObject.put("id_tecnico", tecnicoId)
                jsonObject.put("id_quinta", quintaId)
                jsonObject.put("parcelas", listaParcelas)

                // Convert JSONObject to String
                val jsonObjectString = jsonObject.toString()

                // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
                val requestBody: RequestBody = RequestBody.create(MediaType.get("application/json"),jsonObjectString)

                CoroutineScope(Dispatchers.IO).launch {
                    // Do the POST request and get response
                    val response = VisitaApi().postVisita(requestBody) /** CREAR VISITA API */

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {

                            // Convert raw JSON to pretty JSON using GSON library
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(
                                JsonParser().parse(
                                    response.body()
                                        ?.toString() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                                )
                            )

                            Log.d("Pretty Printed JSON :", prettyJson)

                        } else {

                            Log.e("RETROFIT_ERROR", response.code().toString())

                        }
                    }
                }

            }

        }

        binding.bCancelar.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getNombreQuintas():Array<String>{ /** Funcionalidad provisoria*/
        return listOf("1","2","3").toTypedArray()
    }

    private fun getQuintas():List<Quinta>{ /** Funcionalidad provisoria*/
        val quinta: Quinta = Quinta(1,"sd","sd","dsadfs", 1)
        val quinta1: Quinta = Quinta(2,"sds","ds","fff", 1)
        return listOf(quinta,quinta1)
    }
    private fun getQuintaIdByName(name:String):Int{ /** Funcionalidad provisoria*/
        return name.toInt()
    }

    fun getListaDeParcelas(idVisita:Int):List<Parcela> { /** Funcionalidad provisoria */
        val verdura: Verdura = Verdura(1,arrayOf(2, 3, 4), arrayOf(1, 2, 4),"dsadfs", "Tomate","fssd")
        val par = Parcela(1,2,true,true,idVisita, verdura)
        val par1 = Parcela(2,3,false,true,idVisita, verdura)
        return listOf(par, par1)
     }

    fun createId():Int {    /** CAMBIAR POR UN VERDADERO ID */
        return Random.nextInt(0, 1000)
    }

}