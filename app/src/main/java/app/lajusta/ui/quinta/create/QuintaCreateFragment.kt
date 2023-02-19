package app.lajusta.ui.quinta.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.text.isDigitsOnly
import app.lajusta.R
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.databinding.FragmentQuintaCreateBinding

class QuintaCreateFragment : BaseFragment() {
    private var _binding: FragmentQuintaCreateBinding? = null
    private val binding get() = _binding!!
    // private lateinit var quinta: QuintaCompleta?
    private var familias = listOf<Familia>()

    // TODO parametrizar create para pre-llenarlo
    /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            quinta = bundle.getParcelable("quinta")
        }
    } */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuintaCreateBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()
    }

    private fun fillItem() {
        apiCall(
            { familias = FamiliaApi().getFamilias().body()!! },
            {
                val familias = ArrayAdapter(
                    activity!!, R.layout.spinner_item, familias.map { it.nombre }
                )
                binding.sFamilia.adapter = familias
            },
            "Hubo un error al obtener las familias."
        )
    }

    private fun setClickListeners() {
        binding.bGuardar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val direccion = binding.etDireccion.text.toString().trim()
            val imagen = binding.etImagen.text.toString().trim()
            val familia = familias.find {
                it.nombre == binding.sFamilia.selectedItem.toString()
            }

            if(nombre.isNullOrEmpty()) {
                shortToast("El nombre no puede ser vacío.")
                return@setOnClickListener
            }

            if(familia == null) {
                shortToast("Deben elegir una familia.")
                return@setOnClickListener
            }

            returnSimpleApiCall({
                QuintaApi().postQuinta(
                    Quinta(0, nombre, direccion, imagen, familia.id_fp)
                )
            }, "Hubo un error. La quinta no pudo ser creada.")
        }

        binding.bCancelar.setOnClickListener{ activity?.onBackPressed() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}