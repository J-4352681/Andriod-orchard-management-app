package app.lajusta.ui.bolson.list

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
import app.lajusta.databinding.FragmentBolsonListBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.rondas.Ronda
import app.lajusta.ui.rondas.api.RondaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BolsonListFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentBolsonListBinding? = null
    private val binding get() = _binding!!
    private val bolsonesList = mutableListOf<Bolson>()
    private val familiasList = mutableListOf<Familia>()
    private val rondasList = mutableListOf<Ronda>()
    private val bolsonesCompletosList = mutableListOf<BolsonCompleto>()
    private lateinit var bolsonAdapter: BolsonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBolsonListBinding.inflate(inflater, container, false)

        binding.svBolsones.setOnQueryTextListener(this)
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrearBolson.setOnClickListener {
            view.findNavController().navigate(R.id.bolsonCreateFragment)
        }
    }

    private fun initRecyclerView() {
        bolsonAdapter = BolsonAdapter(bolsonesCompletosList) { bolson: BolsonCompleto ->
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
        lateinit var bolsones: List<Bolson>
        lateinit var familias: List<Familia>
        lateinit var rondas: List<Ronda>

        apiCall(suspend {
            bolsones = BolsonApi().getBolsones().body()!!
            familias = FamiliaApi().getFamilias().body()!!
            rondas = RondaApi().getRondas().body()!!
        }, {
            bolsonesList.clear()
            bolsonesList.addAll(bolsones)

            familiasList.clear()
            familiasList.addAll(familias)

            rondasList.clear()
            rondasList.addAll(rondas)
            fillBolsonesCompletos()
        }, "Hubo un error al actualizar la lista de bolsones.")
    }

    private fun fillBolsonesCompletos() {
        bolsonesCompletosList.clear()
        bolsonesList.forEach { bolson ->
            bolsonesCompletosList.add(
                BolsonCompleto.toBolsonCompleto(
                    bolson,
                    familiasList.find { it.id_fp == bolson.idFp }!!,
                    rondasList.find { it.id_ronda == bolson.idRonda }!!
                )
            )
        }
        bolsonAdapter.notifyDataSetChanged()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) filter(query.lowercase())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}