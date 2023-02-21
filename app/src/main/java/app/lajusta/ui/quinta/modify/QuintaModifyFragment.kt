package app.lajusta.ui.quinta.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import app.lajusta.R
import app.lajusta.databinding.FragmentQuintaModifyBinding
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.model.QuintaCompleta

class QuintaModifyFragment: BaseFragment() {

    private var _binding: FragmentQuintaModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var quinta: QuintaCompleta
    private var familias = listOf<Familia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            quinta = bundle.getParcelable("quinta")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuintaModifyBinding.inflate(
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
        binding.etNombre.setText(quinta.nombre)
        binding.etDireccion.setText(quinta.direccion.toString())
        binding.etImagen.setText(quinta.geoImg.toString())
        apiCall(
            { familias = FamiliaApi().getFamilias().body()!! },
            {
                val familiasAdapter = ArrayAdapter(
                    activity!!, R.layout.spinner_item, familias.map { it.nombre }
                )
                binding.sFamilia.adapter = familiasAdapter
                binding.sFamilia.setSelection(
                    familiasAdapter.getPosition(quinta.familia.nombre)
                )
            },
            "Hubo un error al obtener las familias."
        )
    }

    private fun setClickListeners() {
        binding.btnGoToMap.setOnClickListener {
            val bundle = bundleOf("quinta" to quinta)
            this.findNavController().navigate(R.id.quintaMapaFragment, bundle)
        }

        binding.bBorrar.setOnClickListener {
            returnSimpleApiCall(
                { QuintaApi().deleteQuinta(quinta.id_quinta) },
                "Hubo un error. El bolsón no pudo ser eliminado."
            )
        }

        binding.bGuardar.setOnClickListener {
            quinta.nombre = binding.etNombre.text.toString().trim()
            quinta.direccion = binding.etDireccion.text.toString().trim()
            quinta.geoImg = binding.etImagen.text.toString().trim()
            val familia = familias.find {
                it.nombre == binding.sFamilia.selectedItem.toString()
            }

            if(familia == null) {
                shortToast("Deben elegir una familia.")
                return@setOnClickListener
            }

            quinta.familia = familia

            // TODO verificar validez de campos

            returnSimpleApiCall(
                { QuintaApi().putQuinta(quinta.id_quinta, quinta.toQuinta()) },
                "Hubo un error. El quinta no pudo ser modificado."
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}