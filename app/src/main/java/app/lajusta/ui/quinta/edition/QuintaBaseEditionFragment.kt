package app.lajusta.ui.quinta.edition

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import app.lajusta.R
import app.lajusta.databinding.FragmentQuintaBaseEditionBinding
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.api.VisitaApi

abstract class QuintaBaseEditionFragment: BaseFragment() {
    private var _binding: FragmentQuintaBaseEditionBinding? = null
    protected val binding get() = _binding!!
    protected var quinta = Quinta(0, "", "", "", -1)

    protected var familias = listOf<Familia>()
    protected lateinit var familiasAdapter: ArrayAdapter<Familia>
    protected var visitas = listOf<Visita>()

    private val spName = "User_data"
    protected lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = activity?.getSharedPreferences(spName, Context.MODE_PRIVATE)!!
        arguments?.let { bundle ->
            if (bundle.containsKey("quinta"))
                quinta = bundle.getParcelable("quinta")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuintaBaseEditionBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startFragment()
    }

    private fun startFragment() {
        initNombre()
        initDireccion()
        initGeoImg()

        apiCall(
            {
                familias = FamiliaApi().getFamilias().body()!!
                visitas = VisitaApi().getVisitas().body()!!
            }, {
                initFamiliaSpinner()
                initSubmitAction()
                initDenyAction()
                withApiData()
            },
            "Hubo un error al obtener las familias."
        )
    }

    private fun initNombre() {
        binding.etNombre.setText(quinta.nombre)
        binding.etNombre.afterTextChanged { nombre -> quinta.nombre = nombre.trim() }
    }

    private fun initDireccion() {
        binding.etDireccion.setText(quinta.direccion)
        binding.etDireccion.afterTextChanged { direccion -> quinta.direccion = direccion.trim() }
    }

    private fun initGeoImg() {
        binding.etImagen.setText(quinta.geoImg)
        binding.etImagen.afterTextChanged { url -> quinta.geoImg = url.trim() }
    }

    private fun initDenyAction() {
        binding.bDenyAction.setOnClickListener { denyAction() }
    }

    private fun initFamiliaSpinner() {
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

        binding.sFamilia.setSelection(
            familiasAdapter.getPosition(familias.find { it.id_fp == quinta.fpId })
        )
    }

    private fun initSubmitAction() {
        binding.bSubmitAction.setOnClickListener {
            if (quinta.nombre.isEmpty()) {
                shortToast("El nombre no puede ser vacío.")
                return@setOnClickListener
            }

            commitChange()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected open fun withApiData() {}

    abstract fun commitChange()

    abstract fun denyAction()
}