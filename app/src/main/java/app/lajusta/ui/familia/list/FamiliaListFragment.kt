package app.lajusta.ui.familia.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentFamiliasListBinding
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FamiliaListFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentFamiliasListBinding? = null
    private val binding get() = _binding!!
    private val familiasList = mutableListOf<Familia>()
    private lateinit var familiaAdapter: FamiliaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFamiliasListBinding.inflate(inflater, container, false)

        binding.svFamilia.setOnQueryTextListener(this)
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrearFamilia.setOnClickListener{
            view.findNavController().navigate(R.id.familiaCreateFragment)
        }
    }

    private fun initRecyclerView() {
        familiaAdapter = FamiliaAdapter(familiasList) { familia: Familia ->
            val bundle = bundleOf("familia" to familia)
            this.findNavController().navigate(
                R.id.action_nav_familia_to_familiaModifyFragment, bundle
            )
        }
        binding.rvFamilias.layoutManager = LinearLayoutManager(activity)
        binding.rvFamilias.adapter = familiaAdapter
        filter("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filter(query: String) {
        lateinit var familias: List<Familia>
        apiCall(suspend {
            familias = FamiliaApi().getFamilias().body()!!
        }, {
            familiasList.clear()
            familiasList.addAll(familias)
            familiaAdapter.notifyDataSetChanged()
        }, "Hubo un error al listar los elementos.")
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()) filter(query.lowercase())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}