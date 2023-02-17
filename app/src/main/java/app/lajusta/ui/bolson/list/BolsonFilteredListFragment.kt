package app.lajusta.ui.bolson.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentBolsonListBinding
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.generic.BaseFragment

class BolsonFilteredListFragment: BaseFragment() {

    private var _binding: FragmentBolsonListBinding? = null
    private val binding get() = _binding!!
    private var bolsones = listOf<BolsonCompleto>()
    private lateinit var bolsonAdapter: BolsonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            bolsones = bundle.getParcelableArrayList<BolsonCompleto>("bolsones")?.toList()!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBolsonListBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setClickListeners()
    }

    private fun initRecyclerView() {
        bolsonAdapter = BolsonAdapter(bolsones) { bolson: BolsonCompleto ->
            val bundle = bundleOf("bolson" to bolson)
            this.findNavController().navigate(
                R.id.bolsonModifyFragment, bundle
            )
        }
        binding.rvBolsones.layoutManager = LinearLayoutManager(activity)
        binding.rvBolsones.adapter = bolsonAdapter
    }

    private fun setClickListeners() {
        binding.fabCrearBolson.setOnClickListener {
            view!!.findNavController().navigate(R.id.bolsonCreateFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}