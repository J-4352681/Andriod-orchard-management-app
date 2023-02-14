package app.lajusta.ui.bolson.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentBolsonListBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.api.BolsonProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BolsonListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentBolsonListBinding? = null
    private val binding get() = _binding!!
    private val data = mutableListOf<Bolson>()
    private lateinit var bolsonAdapter: BolsonAdapter
    private lateinit var bolsonProvider: BolsonProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBolsonListBinding.inflate(inflater, container, false)
        bolsonProvider = BolsonProvider(BolsonApi())

        binding.svBolsones.setOnQueryTextListener(this)
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrearBolson.setOnClickListener{
            view.findNavController().navigate(R.id.bolsonCreateFragment)
        }
    }

    private fun initRecyclerView() {
        bolsonAdapter = BolsonAdapter(data) { bolson: Bolson ->
            val bundle = bundleOf("bolson" to bolson)
            this.findNavController().navigate(
                R.id.action_nav_bolson_to_bolsonModifyFragment, bundle
            )
        }
        binding.rvBolsones.layoutManager = LinearLayoutManager(activity)
        binding.rvBolsones.adapter = bolsonAdapter
        filter("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filter(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            lateinit var bolsones: List<Bolson>
            try {
                bolsones = BolsonApi().getBolsones().body()!!
                activity!!.runOnUiThread {
                    data.clear()
                    data.addAll(bolsones)
                    bolsonAdapter.notifyDataSetChanged()
                }
            } catch(e: Exception) {
                activity!!.runOnUiThread {
                    Toast.makeText(activity, "Hubo un error al listar los elementos.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()) filter(query.lowercase())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun shortToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}