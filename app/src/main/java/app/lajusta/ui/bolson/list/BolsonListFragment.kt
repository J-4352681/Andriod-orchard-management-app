package app.lajusta.ui.bolson.list

import android.os.Bundle
import android.util.Log
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
import app.lajusta.ui.bolson.model.BolsonCompleto
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.rondas.Ronda
import app.lajusta.ui.rondas.api.RondaApi
import kotlinx.coroutines.*

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
        filterCompleto("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*private fun filter(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            lateinit var bolsones: List<Bolson>
            try {
                bolsones = BolsonApi().getBolsones().body()!!
                activity!!.runOnUiThread {
                    bolsonesList.clear()
                    bolsonesList.addAll(bolsones)
                    bolsonAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                activity!!.runOnUiThread {
                    shortToast("Hubo un error al listar los elementos.")
                }
            }
        }
    }*/


    private fun filterCompleto(query: String) {

        getBolsonesApiCall()
        getFamiliasApiCall()
        getRondasApiCall()

    }

    //Funciones get apicall

    fun getBolsonesApiCall() {
        apiCall(suspend {
            val bolsones = BolsonApi().getBolsones().body()!!
            activity!!.runOnUiThread {
                bolsonesList.clear()
                bolsonesList.addAll(bolsones)
                fillBolsonesCompletos()
            }
        }, "Hubo un error al actualizar la lista de bolsones.")
    }

    fun getFamiliasApiCall() {
        apiCall(suspend {
            val familias = FamiliaApi().getFamilias().body()!!
            activity!!.runOnUiThread {
                familiasList.clear()
                familiasList.addAll(familias)
                fillBolsonesCompletos()
            }
        }, "Hubo un error al actualizar la lista de familias.")
    }

    fun getRondasApiCall() {
        apiCall(suspend {
            val rondas = RondaApi().getRondas().body()!!
            activity!!.runOnUiThread {
                rondasList.clear()
                rondasList.addAll(rondas)
                fillBolsonesCompletos()
            }
        }, "Hubo un error al actualizar la lista de rondas.")
    }

    fun fillBolsonesCompletos() {
        if (bolsonesList.isNotEmpty() && familiasList.isNotEmpty() && rondasList.isNotEmpty()) {

            var bolsonesCompletos: MutableList<BolsonCompleto> = mutableListOf<BolsonCompleto>()

            bolsonesList.forEach { bolson ->
                var familia = familiasList.find { it.id_fp == bolson.idFp }
                var ronda = rondasList.find { it.id_ronda == bolson.idRonda }
                if (familia != null && ronda != null)
                    bolsonesCompletos.add(
                        BolsonCompleto(
                            bolson.id_bolson,
                            bolson.cantidad,
                            familia!!,
                            ronda!!,
                            bolson.verduras
                        )
                    )
            }

            activity!!.runOnUiThread {
                bolsonesCompletosList.clear()
                bolsonesCompletosList.addAll(bolsonesCompletos)
                bolsonAdapter.notifyDataSetChanged()
            }
        }
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) filterCompleto(query.lowercase())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}