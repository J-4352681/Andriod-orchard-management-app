package app.lajusta.ui.bolson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentBolsonesBinding
import app.lajusta.ui.bolson.api.BolsonesApi
import app.lajusta.ui.bolson.recyclerview.BolsonesAdapter
import app.lajusta.ui.bolson.api.BolsonesProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BolsonesFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentBolsonesBinding? = null
    private val binding get() = _binding!!
    // private lateinit var bolsonesViewModel: BolsonesViewModel
    private val data = mutableListOf<Bolson>()
    private lateinit var bolsonesAdapter: BolsonesAdapter
    private lateinit var bolsonesProvider: BolsonesProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // bolsonesViewModel = ViewModelProvider(this).get(BolsonesViewModel::class.java)
        _binding = FragmentBolsonesBinding.inflate(inflater, container, false)
        bolsonesProvider = BolsonesProvider(BolsonesApi())

        binding.svBolsones.setOnQueryTextListener(this)
        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        bolsonesAdapter = BolsonesAdapter(data)
        binding.rvBolsones.layoutManager = LinearLayoutManager(activity)
        binding.rvBolsones.adapter = bolsonesAdapter
        filter("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filter(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val bolsones = bolsonesProvider.getBolsones()
            activity!!.runOnUiThread {
                data.clear()
                data.addAll(bolsones)
                bolsonesAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()) filter(query.lowercase())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // A completar
        return true
    }
}