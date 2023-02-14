package app.lajusta.ui.bolson.modify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentBolsonModifyBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.verdura.Verdura
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BolsonModifyFragment() : Fragment() {

    private var _binding: FragmentBolsonModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var bolson: Bolson
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
            CoroutineScope(Dispatchers.IO).launch {
                try { BolsonApi().deleteBolson(bolson.id_bolson) }
                catch (e: Exception) { activity!!.runOnUiThread { shortToast(
                    "Hubo un error. El elemento no pudo ser eliminado."
                ) } }
                finally { activity!!.runOnUiThread { activity!!.onBackPressed() } }
            }
        }

        binding.bGuardar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try { BolsonApi().putBolson(bolson.id_bolson, bolson) }
                catch(e: Exception) { activity!!.runOnUiThread { shortToast(
                    "Hubo un error. El elemento no pudo ser modificado."
                ) } }
                finally { activity!!.runOnUiThread { activity!!.onBackPressed() } }
            }
        }
    }

    private fun fillItem() {
        binding.tvTitle.text = "Modificando bolsón " + bolson.id_bolson.toString()
        binding.etCantidad.setText(bolson.cantidad.toString())
        binding.etFamilia.setText(bolson.idFp.toString())
        binding.etRonda.setText(bolson.idRonda.toString())
        initRecyclerView()
    }

    private fun initRecyclerView() {
        verduraBolsonAdapter = VerduraBolsonAdapter(
            bolson.verduras
        ) { position: Int ->
            bolson.verduras = bolson.verduras.filterIndexed { i: Int, _: Verdura -> i != position }
            verduraBolsonAdapter.notifyDataSetChanged()
            shortToast(bolson.verduras.toString())
        }
        binding.rvVerduras.layoutManager = LinearLayoutManager(activity)
        binding.rvVerduras.adapter = verduraBolsonAdapter
    }

    private fun shortToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun longToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}