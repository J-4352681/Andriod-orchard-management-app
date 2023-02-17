package app.lajusta.ui.bolson.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentBolsonModifyBinding
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.model.BolsonCompleto
import app.lajusta.ui.generic.BaseFragment

class BolsonModifyFragment: BaseFragment() {

    private var _binding: FragmentBolsonModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var bolson: BolsonCompleto
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
    ): View? {
        _binding = FragmentBolsonModifyBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()

        binding.bBorrar.setOnClickListener {
            returnSimpleApiCall(
                { BolsonApi().deleteBolson(bolson.id_bolson) },
                "Hubo un error. El bolsón no pudo ser eliminado."
            )
        }

        binding.bGuardar.setOnClickListener {
            bolson.ronda.id_ronda = binding.etRonda.text.toString().toInt()
            bolson.familia.id_fp = binding.etFamilia.text.toString().toInt()
            bolson.cantidad = binding.etCantidad.text.toString().toInt()
            bolson.verduras = verduraBolsonAdapter.getVerduras()

            // TODO verificar validez de campos

            returnSimpleApiCall(
                { BolsonApi().putBolson(bolson.toBolson()) },
                "Hubo un error. El bolson no pudo ser modificado."
            )
        }
    }

    private fun fillItem() {
        binding.tvTitle.text = "Modificando bolsón " + bolson.id_bolson.toString()
        binding.etCantidad.setText(bolson.cantidad.toString())
        binding.etFamilia.setText(bolson.familia.id_fp.toString()) //Estos se pueden cambiar por bolson.familia.nombre y no deberian de ser editables en el futuro creo
        binding.etRonda.setText(bolson.ronda.id_ronda.toString())  // Cambiar por bolson.ronda.fecha_inicio
        initRecyclerView()
    }

    private fun initRecyclerView() {
        verduraBolsonAdapter = VerduraBolsonAdapter(bolson.verduras)
        binding.rvVerduras.layoutManager = LinearLayoutManager(activity)
        binding.rvVerduras.adapter = verduraBolsonAdapter
    }
}