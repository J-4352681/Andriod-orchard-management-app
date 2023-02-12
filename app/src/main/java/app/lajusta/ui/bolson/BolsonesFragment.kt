package app.lajusta.ui.bolson

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentBolsonesBinding
import app.lajusta.ui.bolson.api.BolsonesAPIService
import app.lajusta.ui.bolson.recyclerview.BolsonesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BolsonesFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentBolsonesBinding? = null
    private val binding get() = _binding!!
    // private lateinit var bolsonesViewModel: BolsonesViewModel
    private val data = mutableListOf<Bolson>()
    private lateinit var bolsonesAdapter: BolsonesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // bolsonesViewModel = ViewModelProvider(this).get(BolsonesViewModel::class.java)
        _binding = FragmentBolsonesBinding.inflate(inflater, container, false)

        binding.svBolsones.setOnQueryTextListener(this)
        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        bolsonesAdapter = BolsonesAdapter(data)
        binding.rvBolsones.layoutManager = LinearLayoutManager(activity)
        // binding.rvBolsones.adapter = BolsonesAdapter(bolsonesViewModel.bolsones.value.orEmpty())
        binding.rvBolsones.adapter = bolsonesAdapter
        filter("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.120:80/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun filter(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(BolsonesAPIService::class.java).getBolsones("bolson")
            val callBody = call.body()
            activity!!.runOnUiThread {
                if(call.isSuccessful) {
                    Toast.makeText(activity, "Error al obtener datos.", Toast.LENGTH_SHORT)
                    data.clear()
                    if(callBody != null) data.addAll(callBody)
                    bolsonesAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "Error al obtener datos.", Toast.LENGTH_SHORT)
                }
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