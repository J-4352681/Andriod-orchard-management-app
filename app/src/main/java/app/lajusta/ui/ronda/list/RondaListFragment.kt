package app.lajusta.ui.ronda.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentRondaListBinding
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi

class RondaListFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentRondaListBinding? = null
    private val binding get() = _binding!!
    private var familias = listOf<Familia>()
    private var rondasApi = listOf<Ronda>()
    private val rondas = mutableListOf<Ronda>()
    private val rondasOriginal = mutableListOf<Ronda>()
    private var rondasArg: MutableList<Ronda>? = null
    private lateinit var rondaAdapter: RondaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            val data = bundle.getParcelableArrayList<Ronda>("rondas")
            if(data != null) rondasArg = data.toMutableList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRondaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrearRonda.setOnClickListener {
            this.findNavController().navigate(R.id.rondaCreateFragment)
        }

        binding.svRondas.setOnQueryTextListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        rondaAdapter = RondaAdapter(rondas) { ronda: Ronda ->
            val bundle = bundleOf("ronda" to ronda)
            this.findNavController().navigate(R.id.rondaModifyFragment, bundle)
        }
        binding.rvRondas.layoutManager = LinearLayoutManager(activity)
        binding.rvRondas.adapter = rondaAdapter
        listInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listInit() {
        apiCall(suspend {
            rondasApi = RondaApi().getRondas().body()!!
        }, {
            rondas.clear()
            if(rondasArg != null) rondas.addAll(rondasArg!!)
            else rondas.addAll(rondasApi)

            rondasOriginal.clear()
            rondasOriginal.addAll(rondas)

            rondaAdapter.notifyDataSetChanged()
        }, "Hubo un error al actualizar la lista de rondas.")
    }

    private fun filter(query: String?) {
        Ronda.filter(rondas, rondasOriginal, query)
        rondaAdapter.notifyDataSetChanged()
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(query: String?): Boolean {
        filter(query!!.lowercase())
        return true
    }
}