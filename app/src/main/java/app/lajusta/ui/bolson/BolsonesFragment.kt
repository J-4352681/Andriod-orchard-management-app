package app.lajusta.ui.bolson

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentBolsonesBinding
import app.lajusta.ui.bolson.recyclerview.BolsonesAdapter

class BolsonesFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentBolsonesBinding? = null
    private val binding get() = _binding!!
    private lateinit var bolsonesViewModel: BolsonesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bolsonesViewModel = ViewModelProvider(this).get(BolsonesViewModel::class.java)
        _binding = FragmentBolsonesBinding.inflate(inflater, container, false)

        binding.svBolsones.setOnQueryTextListener(this)
        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvBolsones.layoutManager = LinearLayoutManager(activity)
        binding.rvBolsones.adapter = BolsonesAdapter(bolsonesViewModel.bolsones.value.orEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // A completar
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // A completar
        return true
    }
}