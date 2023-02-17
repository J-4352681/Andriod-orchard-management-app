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
import app.lajusta.ui.rondas.Ronda
import app.lajusta.ui.rondas.api.RondaApi
import kotlinx.coroutines.*

class BolsonListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentBolsonListBinding? = null
    private val binding get() = _binding!!
    //private val bolsonesList = mutableListOf<Bolson>()
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

        val coroutineScope = CoroutineScope(Dispatchers.IO)

        //Antes llamaba a las funciones
        //var promesa_bolsones: Deferred<List<Bolson>> = getBolsones(coroutineScope)
        //var promesa_rondas: Deferred<List<Ronda>> = getRondas(coroutineScope)
        //var promesa_familias: Deferred<List<Familia>> = getFamilias(coroutineScope)

        var promesa_bolsones: Deferred<List<Bolson>> = coroutineScope.async {
            var bolsones: List<Bolson> = BolsonApi().getBolsones().body()!!
            return@async bolsones
        }

        var promesa_rondas: Deferred<List<Ronda>> = coroutineScope.async {
            var ronda = RondaApi().getRondas().body()!!
            return@async ronda
        }

        var promesa_familias: Deferred<List<Familia>> = coroutineScope.async {
            var familia = FamiliaApi().getFamilias().body()!!
            return@async familia
        }

        coroutineScope.launch {

            try {

                var bolsonesCompletos: MutableList<BolsonCompleto> = mutableListOf<BolsonCompleto>()
                var bol = promesa_bolsones.await()
                var fam = promesa_familias.await()
                var ron = promesa_rondas.await()

                joinAll()

                bol.forEach { bolson ->
                    var familia = fam.find { it.id_fp == bolson.idFp }
                    var ronda = ron.find { it.id_ronda == bolson.idRonda }
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

            } catch (e: Exception) {
                Log.e("ERROR MESSAGE", e.message.orEmpty())
                Log.e("stack", e.stackTraceToString())
                activity!!.runOnUiThread {
                    shortToast("Hubo un error al listar los elementos.")
                }
            }
        }
    }

    //FUNCIONES GET DEFFERED
    fun getBolsones(scope: CoroutineScope): Deferred<List<Bolson>> {
        return scope.async {
            lateinit var bolsones: List<Bolson>
            try {
                bolsones = BolsonApi().getBolsones().body()!!
                return@async bolsones
            } catch (e: Exception) {
                Log.e("RETROFIT_ERROR (bols)", e.message.orEmpty())
                Log.e("stack", e.stackTraceToString())
                activity!!.runOnUiThread {
                    shortToast("Hubo un error al listar los elementos.")
                }
            }
            return@async bolsones
        }
    }

    private fun getRondas(scope: CoroutineScope): Deferred<List<Ronda>> {

        val rn = scope.async {
            lateinit var ronda: List<Ronda>
            try {
                ronda = RondaApi().getRondas().body()!!

            } catch (e: Exception) {
                Log.e("RETROFIT_ERROR (ron)", e.message.orEmpty())
            }
            return@async ronda
        }
        return rn

    }

    private fun getFamilias(scope: CoroutineScope): Deferred<List<Familia>> {

        val fm = scope.async {
            lateinit var familia: List<Familia>
            try {
                familia = FamiliaApi().getFamilias().body()!!

            } catch (e: Exception) {
                Log.e("RETROFIT_ERROR (fam)", e.message.orEmpty())
            }
            return@async familia
        }
        return fm
    }

    //FUNCIONES GET DEFFERED END


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) filterCompleto(query.lowercase())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun shortToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}