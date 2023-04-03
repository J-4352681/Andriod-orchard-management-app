package app.lajusta.ui.verdura.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentVerduraListBinding
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.verdura.api.VerduraApi

class VerduraListFragment: BaseFragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentVerduraListBinding? = null
    private val binding get() = _binding!!
    private var familias = listOf<Familia>()
    private var verdurasApi = listOf<Verdura>()
    private val verduras = mutableListOf<Verdura>()
    private val verdurasOriginal = mutableListOf<Verdura>()
    private var verdurasArg: MutableList<Verdura>? = null
    private lateinit var verduraAdapter: VerduraAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            val data = bundle.getParcelableArrayList<Verdura>("verduras")
            if(data != null) verdurasArg = data.toMutableList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerduraListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrearVerdura.setOnClickListener {
            this.findNavController().navigate(R.id.verduraCreateFragment)
        }

        binding.svVerduras.setOnQueryTextListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        verduraAdapter = VerduraAdapter(verduras) { verdura: Verdura ->
            val bundle = bundleOf("verdura" to verdura)
            this.findNavController().navigate(R.id.verduraModifyFragment, bundle)
        }
        binding.rvVerduras.layoutManager = LinearLayoutManager(activity)
        binding.rvVerduras.adapter = verduraAdapter
        listInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listInit() {
        apiCallProgressBar(suspend {
            verdurasApi = VerduraApi().getVerduras().body()!!
        }, {
            verduras.clear()
            if(verdurasArg != null) verduras.addAll(verdurasArg!!)
            else verduras.addAll(verdurasApi)

            verdurasOriginal.clear()
            verdurasOriginal.addAll(verduras)

            actualizarListaUI("No se encontraron verduras en el sistema")
        }, "Hubo un error al actualizar la lista de verduras.", binding.progressBar)
    }

    private fun filter(query: String?) {
        Verdura.filter(verduras, verdurasOriginal, query)
        actualizarListaUI("No se encontraron verduras que coincidan en el sistema")
    }

    private fun actualizarListaUI ( emptyMessage:String ) {
        verduras.sort()
        verduraAdapter.notifyDataSetChanged()
        //if (verduraAdapter.itemCount == 0) shortToast(emptyMessage)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(query: String?): Boolean {
        filter(query!!.lowercase())
        return true
    }
}