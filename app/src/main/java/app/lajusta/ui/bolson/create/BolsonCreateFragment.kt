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
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.generic.BaseFragment
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


class BolsonCreateFragment : BaseFragment() {

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


    private val verdura: Verdura = Verdura(1, arrayOf(2, 3, 4), arrayOf(1, 2, 4),"dsadfs", "Tomate","fssd")
    private val verduras: List<Verdura> = listOf(verdura,verdura,verdura,verdura,verdura,verdura,verdura)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bGuardar.setOnClickListener{

            val ronda = binding.etRonda.text.toString().trim()
            val familia = binding.etFamilia.text.toString().trim() /** CAMBIAR POR UNA COMBOBOX QUE TAMBIEN NOS DE EL ID*/
            val cantidad = binding.etCantidad.text.toString()
            //val verdurass: List<Verdura> = binding.rvVerduras.adapter.getList()

            if ( ronda.isNotEmpty() && familia.isNotEmpty() && cantidad.isNotEmpty() && verduras.size == 7) {

                Toast.makeText(activity, "Datos correctos. Intento de postear.", Toast.LENGTH_SHORT).show()

                val idBolson = createId()
                val bolson = Bolson(idBolson,cantidad.toInt(),familia.toInt(),ronda.toInt(),verduras)

                simpleApiCall(
                    { BolsonApi().postBolson(bolson)},
                    "Hubo un error. El bolson no pudo ser creado."
                )

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