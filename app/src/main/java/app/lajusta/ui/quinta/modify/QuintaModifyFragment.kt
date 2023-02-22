package app.lajusta.ui.quinta.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import app.lajusta.R
import app.lajusta.databinding.FragmentQuintaModifyBinding
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.quinta.model.QuintaCompleta

class QuintaModifyFragment: BaseFragment() {

    private var _binding: FragmentQuintaModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var quinta: QuintaCompleta

    private var familias = listOf<Familia>()
    private lateinit var familiasAdapter: ArrayAdapter<Familia>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            quinta = bundle.getParcelable("quinta")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuintaModifyBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()

        binding.etDireccion.afterTextChanged { direccion -> quinta.direccion = direccion.trim() }
        binding.etImagen.afterTextChanged { url -> quinta.geoImg = url.trim() }
        binding.etNombre.afterTextChanged { nombre -> quinta.nombre = nombre.trim() }
    }

    private fun fillItem() {
        binding.etNombre.setText(quinta.nombre)
        binding.etDireccion.setText(quinta.direccion)
        binding.etImagen.setText(quinta.geoImg)

        apiCall(
            { familias = FamiliaApi().getFamilias().body()!! },
            {
                familiasAdapter = ArrayAdapter(activity!!, R.layout.spinner_item, familias)
                binding.sFamilia.adapter = familiasAdapter
                binding.sFamilia.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?, p1: View?, position: Int, p3: Long
                        ) {
                            val familiaSeleccionada = binding.sFamilia.selectedItem as Familia
                            quinta.familia = familiaSeleccionada
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {}
                    }

                binding.sFamilia.setSelection(
                    familiasAdapter.getPosition(quinta.familia)
                )

                setClickListeners()
            },
            "Hubo un error al obtener las familias."
        )
    }

    private fun setClickListeners() {
        binding.btnGoToMap.setOnClickListener {
            val bundle = bundleOf("quinta" to quinta)
            this.findNavController().navigate(R.id.quintaMapaFragment, bundle)
        }

        binding.btnAddVisita.setOnClickListener {
            //val visita = VisitaCompleta(0, ArrayedDate.todayArrayed(), "", null, )
            val bundle = bundleOf("quinta" to quinta)
            this.findNavController().navigate(R.id.visitasForQuintaCreateFragment, bundle)
        }

        binding.bBorrar.setOnClickListener {
            returnSimpleApiCall(
                { QuintaApi().deleteQuinta(quinta.id_quinta) },
                "Hubo un error. El bolsón no pudo ser eliminado."
            )
        }

        binding.bGuardar.setOnClickListener {
            if(quinta.nombre.isEmpty()) {
                shortToast("El nombre no puede ser vacío.")
                return@setOnClickListener
            }

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