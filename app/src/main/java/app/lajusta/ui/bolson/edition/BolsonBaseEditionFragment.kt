package app.lajusta.ui.bolson.edition

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentBolsonBaseEditionBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.PrefilledBolson
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi

abstract class BolsonBaseEditionFragment: BaseFragment() {
    private var _binding: FragmentBolsonBaseEditionBinding? = null
    protected val binding get() = _binding!!
    protected var bolson = Bolson(0, 0, -1, -1, mutableListOf())
    private var prefilledBolson: PrefilledBolson? = null

    private var familias = listOf<Familia>()
    private lateinit var familiasAdapter: ArrayAdapter<Familia>
    private var rondas = listOf<Ronda>()
    private lateinit var rondasAdapter: ArrayAdapter<Ronda>

    private lateinit var verduraBolsonAdapter: VerduraBolsonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            if(bundle.containsKey("bolson"))
                bolson = bundle.getParcelable("bolson")!!
            if(bundle.containsKey("prefilledBolson"))
                prefilledBolson = bundle.getParcelable("prefilledBolson")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBolsonBaseEditionBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startFragment()
    }

    private fun startFragment() {
        initCantidad()
        apiCall(
            {
                familias = FamiliaApi().getFamilias().body()!!
                rondas = RondaApi().getRondas().body()!!
            }, {
                initFamiliasSpinner()
                initRondasSpinner()
                setClickListeners()
                initRecyclerView()
                prefillBolson()
            }, "No se pudieron obtener las familias o rondas."
        )
    }

    private fun initCantidad() {
        binding.etCantidad.setText(bolson.cantidad.toString())
        binding.etCantidad.afterTextChanged { cantidad ->
            bolson.cantidad = if (cantidad != "") cantidad.toInt() else 0
        }
    }

    private fun initFamiliasSpinner() {
        val idFamiliaSeleccionada = bolson.idFp
        familiasAdapter = ArrayAdapter(activity!!, R.layout.spinner_item, familias)
        binding.sFamilia.adapter = familiasAdapter
        binding.sFamilia.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, p1: View?, position: Int, p3: Long
                ) {
                    val familiaSeleccionada = binding.sFamilia.selectedItem as Familia
                    bolson.idFp = familiaSeleccionada.id_fp
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        binding.sFamilia.setSelection(
            familiasAdapter.getPosition(familias.find { it.id_fp == idFamiliaSeleccionada })
        )
    }

    private fun initRondasSpinner() {
        val idRondaSeleccionada = bolson.idRonda
        rondasAdapter = ArrayAdapter(activity!!, R.layout.spinner_item, rondas)
        binding.sRonda.adapter = rondasAdapter
        binding.sRonda.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, p1: View?, position: Int, p3: Long
                ) {
                    val rondaSeleccionada = binding.sRonda.selectedItem as Ronda
                    bolson.idRonda = rondaSeleccionada.id_ronda
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        binding.sRonda.setSelection(
            rondasAdapter.getPosition(rondas.find { it.id_ronda == idRondaSeleccionada })
        )
    }

    private fun setClickListeners() {
        findNavController().currentBackStackEntry?.savedStateHandle!!
            .getLiveData<Bolson>("bolson").observe(viewLifecycleOwner) { bolson = it }

        binding.btnAgregarVerdura.setOnClickListener {
            val bundle = bundleOf("bolson" to bolson)
            this.findNavController().navigate(R.id.verduraSelectFragment, bundle)
        }

        binding.bDenyAction.setOnClickListener { denyAction() }

        binding.bSubmitAction.setOnClickListener {
            val cantidadtxt = binding.etCantidad.text.toString()

            if(cantidadtxt.isEmpty() || bolson.cantidad == 0) {
                shortToast("Debe escribir una cantidad igual o mayor a 1")
                return@setOnClickListener
            } else bolson.cantidad = cantidadtxt.toInt()

            if(bolson.verduras.size < 7) {
                shortToast("Deberían ser al menos 7 verduras")
                AlertDialog.Builder(activity!!)
                    .setMessage("¿Seguro desea proceder con menos de 7 verduras?")
                    .setCancelable(false)
                    .setPositiveButton("Si") { _, _ ->
                        commitChange()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
                return@setOnClickListener
            }

            commitChange()
        }
    }

    private fun initRecyclerView() {
        verduraBolsonAdapter = VerduraBolsonAdapter(bolson.verduras, true)
        binding.rvVerduras.layoutManager = LinearLayoutManager(activity)
        binding.rvVerduras.adapter = verduraBolsonAdapter
    }

    private fun prefillBolson() {
        if(prefilledBolson != null) {
            println(prefilledBolson!!.cantidad)
            if(prefilledBolson!!.cantidad != null) {
                binding.etCantidad.setText(prefilledBolson?.cantidad!!.toString())
                if(prefilledBolson!!._blockFields) binding.etCantidad.isEnabled = false
            }
            if(prefilledBolson!!.idRonda != null) {
                binding.sRonda.setSelection(
                    rondasAdapter.getPosition(rondas.find {
                        it.id_ronda == prefilledBolson!!.idRonda
                    })
                )
                if(prefilledBolson!!._blockFields) binding.sRonda.isEnabled = false
            }
            if(prefilledBolson!!.idFp != null) {
                binding.sFamilia.setSelection(
                    familiasAdapter.getPosition(familias.find {
                        it.id_fp == prefilledBolson!!.idFp
                    })
                )
                if(prefilledBolson!!._blockFields) binding.sFamilia.isEnabled = false
            }
            if(prefilledBolson!!.verduras != null) {
                bolson.verduras.clear()
                bolson.verduras.addAll(prefilledBolson!!.verduras!!)
                verduraBolsonAdapter.notifyDataSetChanged()
                if(prefilledBolson!!._blockFields) {
                    verduraBolsonAdapter.setActive(false)
                    binding.btnAgregarVerdura.isEnabled = false
                }
            }
            if(prefilledBolson!!._blockSubmitAction) {
                binding.bSubmitAction.isEnabled = false
                binding.bDenyAction.isEnabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected abstract fun denyAction()

    protected abstract fun commitChange()
}