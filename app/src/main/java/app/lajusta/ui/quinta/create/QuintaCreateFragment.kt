package app.lajusta.ui.quinta.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import app.lajusta.R
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.databinding.FragmentQuintaCreateBinding
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.quinta.model.QuintaCompletaPrefill

class QuintaCreateFragment : BaseFragment() {
    private var _binding: FragmentQuintaCreateBinding? = null
    private val binding get() = _binding!!
    private val quinta = Quinta(0, "", "", "", -1)
    private var prefilledQuinta: QuintaCompletaPrefill? = null

    private var familias = listOf<Familia>()
    private lateinit var familiasAdapter: ArrayAdapter<Familia>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            val data = bundle.getParcelable<QuintaCompletaPrefill>("quinta")
            if(data != null) prefilledQuinta = data
        }
    }

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

        binding.etDireccion.afterTextChanged { direccion -> quinta.direccion = direccion.trim() }
        binding.etImagen.afterTextChanged { url -> quinta.geoImg = url.trim() }
        binding.etNombre.afterTextChanged { nombre -> quinta.nombre = nombre.trim() }
    }

    private fun fillItem() {
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
                            quinta.fpId = familiaSeleccionada.id_fp
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {}
                    }

                if(prefilledQuinta != null && prefilledQuinta!!.familia != null) {
                    binding.sFamilia.setSelection(
                        familiasAdapter.getPosition(prefilledQuinta!!.familia!!)
                    )
                }

                setClickListeners()
            },
            "Hubo un error al obtener las familias."
        )

        if(prefilledQuinta != null) {
            if(prefilledQuinta!!.geoImg != null)
                binding.etImagen.setText(prefilledQuinta!!.geoImg)
            if(prefilledQuinta!!.nombre != null)
                binding.etNombre.setText(prefilledQuinta!!.nombre)
            if(prefilledQuinta!!.direccion != null)
                binding.etDireccion.setText(prefilledQuinta!!.direccion)
        }
    }

    private fun setClickListeners() {
        binding.bGuardar.setOnClickListener {
            if(quinta.nombre.isEmpty()) {
                shortToast("El nombre no puede ser vacío.")
                return@setOnClickListener
            }

            if(quinta.fpId == -1) {
                shortToast("Deben elegir una familia.")
                return@setOnClickListener
            }

            returnSimpleApiCall(
                { QuintaApi().postQuinta(quinta) },
                "Hubo un error. La quinta no pudo ser creada."
            )
        }

        binding.bCancelar.setOnClickListener{ activity?.onBackPressed() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}