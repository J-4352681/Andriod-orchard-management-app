package app.lajusta.ui.bolson.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentBolsonListBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.api.BolsonProvider
import app.lajusta.ui.bolson.modify.BolsonModifyFragment
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

    private fun initRecyclerView() {
        bolsonAdapter = BolsonAdapter(data) { bolson: Bolson ->
            Toast.makeText(
                activity,
                bolson.id_bolson.toString(),
                Toast.LENGTH_SHORT
            ).show()
            parentFragmentManager.beginTransaction().replace(this.id, BolsonModifyFragment(bolson)).commit()
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
            val bolsones = bolsonProvider.getBolsones()
            activity!!.runOnUiThread {
                data.clear()
                data.addAll(bolsones)
                bolsonAdapter.notifyDataSetChanged()
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
}