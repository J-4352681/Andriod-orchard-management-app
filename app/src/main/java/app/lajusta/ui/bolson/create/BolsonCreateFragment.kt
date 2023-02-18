package app.lajusta.ui.bolson.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.lajusta.databinding.FragmentBolsonCreateBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.generic.BaseFragment
import kotlin.random.Random


class BolsonCreateFragment : BaseFragment() {
    private var _binding: FragmentBolsonCreateBinding? = null
    private val binding get() = _binding!!
    // private lateinit var bolson: BolsonCompleto?

    // TODO parametrizar create para pre-llenarlo
    /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            bolson = bundle.getParcelable("bolson")
        }
    } */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBolsonCreateBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    private val verdura: Verdura = Verdura(1, arrayOf(2, 3, 4), arrayOf(1, 2, 4),"dsadfs", "Tomate","fssd")
    private val verduras: List<Verdura> = listOf(verdura,verdura,verdura,verdura,verdura,verdura,verdura) /** CAMBIAR POR EL CRUD DE VERDURAS */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // fillItem()

        binding.bGuardar.setOnClickListener{
            val ronda = binding.etRonda.text.toString().trim()
            val familia = binding.etFamilia.text.toString().trim() /** CAMBIAR POR UNA COMBOBOX QUE TAMBIEN NOS DE EL ID*/
            val cantidad = binding.etCantidad.text.toString()

            if(ronda.isNullOrEmpty()) {
                shortToast("Debe seleccionar una ronda")
                return@setOnClickListener
            }

            if(familia.isNullOrEmpty()) {
                shortToast("Debe seleccionar una familia")
                return@setOnClickListener
            }

            if(cantidad.isNullOrEmpty()) {
                shortToast("Debe escribir una cantidad")
                return@setOnClickListener
            }

            if(verduras.size < 7) {
                shortToast("Deben ser al menos 7 verduras")
                return@setOnClickListener
            }

            returnSimpleApiCall({
                BolsonApi().postBolson(
                    Bolson(0,cantidad.toInt(),familia.toInt(),ronda.toInt(),verduras)
                )
            }, "Hubo un error. El bolsón no pudo ser creado.")
        }

        binding.bCancelar.setOnClickListener{ activity?.onBackPressed() }
    }

    /* private fun fillItem() {
        if(bolson != null)
    } */

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}