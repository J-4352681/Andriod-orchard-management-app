package app.lajusta.ui.bolson

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentBolsonesBinding
import app.lajusta.ui.bolson.recyclerview.BolsonesAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BolsonesFragment : Fragment() {

    private var _binding: FragmentBolsonesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bolsonViewModel =
            ViewModelProvider(this).get(BolsonesViewModel::class.java)

        _binding = FragmentBolsonesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /* val textView: TextView = binding.textBolson
        bolsonViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        } */

        binding.rvBolsones.layoutManager = LinearLayoutManager(parentFragment?.context)
        binding.rvBolsones.adapter = BolsonesAdapter(bolsonViewModel.bolsones.value.orEmpty())

        return root
    }

    /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvBolsones.layoutManager = LinearLayoutManager(parentFragment.context)
        binding.rvBolsones.adapter = BolsonesAdapter(bolsonesViewModel.bolsones.value)
    } */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchByText(query:String) {
        // A completar
    }
}