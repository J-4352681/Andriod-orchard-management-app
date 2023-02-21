package app.lajusta.ui.bolson.modify

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentBolsonModifyBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi
import app.lajusta.ui.verdura.Verdura

class BolsonModifyFragment: BaseFragment() {

    private var _binding: FragmentBolsonModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var bolson: Bolson

    private var familias = listOf<Familia>()
    private lateinit var familiasAdapter: ArrayAdapter<Familia>
    private var rondas = listOf<Ronda>()
    private lateinit var rondasAdapter: ArrayAdapter<Ronda>

    private val verdurasTotales = mutableListOf<Verdura>()
    private lateinit var verduraBolsonAdapter: VerduraBolsonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            bolson = bundle.getParcelable("bolson")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBolsonModifyBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
    }

    private fun fillItem() {
        verdurasTotales.addAll(bolson.verduras)

        apiCall(
            {
                familias = FamiliaApi().getFamilias().body()!!
                rondas = RondaApi().getRondas().body()!!
            }, {
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

                setClickListeners()
            }, "No se pudieron obtener las familias."
        )
        binding.etCantidad.setText(bolson.cantidad.toString())

        initRecyclerView()
    }

    private fun initRecyclerView() {
        verduraBolsonAdapter = VerduraBolsonAdapter(verdurasTotales, bolson)
        binding.rvVerduras.layoutManager = LinearLayoutManager(activity)
        binding.rvVerduras.adapter = verduraBolsonAdapter
    }

    private fun setClickListeners() {
        val stateHandle: SavedStateHandle =
            findNavController().currentBackStackEntry?.savedStateHandle!!

        stateHandle.getLiveData<Bolson>("bolson")
            .observe(viewLifecycleOwner) { data ->
                bolson = data
                binding.sFamilia.setSelection(
                    familiasAdapter.getPosition(familias.find { it.id_fp == bolson.idFp })
                )
                binding.sRonda.setSelection(
                    rondasAdapter.getPosition(rondas.find { it.id_ronda == bolson.idRonda })
                )
                verdurasTotales.clear()
                verdurasTotales.addAll(bolson.verduras)
                verduraBolsonAdapter.notifyDataSetChanged()
            }

        binding.bBorrar.setOnClickListener {
            returnSimpleApiCall(
                { BolsonApi().deleteBolson(bolson.id_bolson) },
                "Hubo un error. El bolsón no pudo ser eliminado."
            )
        }

        binding.bGuardar.setOnClickListener {
            val cantidadtxt = binding.etCantidad.text.toString()

            if(cantidadtxt.isEmpty()) {
                shortToast("Debe escribir una cantidad")
                return@setOnClickListener
            } else bolson.cantidad = cantidadtxt.toInt()

            if(bolson.verduras.size < 7) {
                shortToast("Deberían ser al menos 7 verduras")
                AlertDialog.Builder(activity!!)
                    .setMessage("¿Seguro desea proceder con menos de 7 verduras?")
                    .setCancelable(false)
                    .setPositiveButton("Si") { dialog, id ->
                        commitChange()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
                return@setOnClickListener
            }

            commitChange()
        }

        binding.btnAgregarVerdura.setOnClickListener {
            val bundle = bundleOf("bolson" to bolson)
            this.findNavController().navigate(R.id.verduraSelectFragment, bundle)
        }
    }

    private fun commitChange() =
        returnSimpleApiCall(
            { BolsonApi().putBolson(bolson) },
            "Hubo un error. El bolsón no pudo ser creado."
        )
}