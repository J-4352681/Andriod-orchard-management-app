package app.lajusta.ui.bolson.create

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.lajusta.R
import app.lajusta.databinding.FragmentBolsonCreateBinding
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.bolson.api.BolsonApi
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import org.json.JSONObject
import okhttp3.RequestBody
import kotlin.random.Random


class BolsonCreateFragment : Fragment(R.layout.fragment_bolson_create) {

    private var _binding: FragmentBolsonCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBolsonCreateBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }


    private val verdura: Verdura = Verdura(1,"sd","sd","dsadfs", "Tomate","fssd")
    private val verduras: List<Verdura> = listOf(verdura,verdura,verdura,verdura,verdura,verdura,verdura)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bGuardar.setOnClickListener{

            val ronda = binding.etRonda.text.toString().trim()
            val familia = binding.etFamilia.text.toString().trim() /** CAMBIAR POR UNA COMBOBOX QUE TAMBIEN NOS DE EL ID*/
            val cantidad = binding.etCantidad.text.toString().trim()
            //val verdurass: List<Verdura> = binding.rvVerduras.adapter.getList()

            if ( ronda.isNotEmpty() && familia.isNotEmpty() && cantidad.isNotEmpty() && verduras.isNotEmpty()) {        /**CAMBIAR PARA QUE TENGA QUE TENER AL MENOS 7 VERDURAS**/

                Toast.makeText(activity, "Datos correctos. Intento de postear.", Toast.LENGTH_SHORT).show()

                val idBolson = createId()

                // Create JSON using JSONObject
                val jsonObject = JSONObject()
                jsonObject.put("id_bolson", idBolson)
                jsonObject.put("cantidad", cantidad)
                jsonObject.put("idFp", familia)
                jsonObject.put("idRonda", ronda)
                jsonObject.put("verduras", verduras)

                // Convert JSONObject to String
                val jsonObjectString = jsonObject.toString()

                // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
                val requestBody:RequestBody = RequestBody.create(MediaType.get("application/json"),jsonObjectString)

                CoroutineScope(Dispatchers.IO).launch {
                    // Do the POST request and get response
                    val response = BolsonApi().postBolson(requestBody)

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

            } else {
                Toast.makeText(activity, "Ingrese todos los datos y al menos 7 verduras.", Toast.LENGTH_SHORT).show()
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

    fun createId():Int {    /** CAMBIAR POR UN VERDADERO ID */
        return Random.nextInt(0, 1000)
    }
}