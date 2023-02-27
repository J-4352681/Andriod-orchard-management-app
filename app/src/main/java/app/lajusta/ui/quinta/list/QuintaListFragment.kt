package app.lajusta.ui.quinta.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentQuintaListBinding
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.model.QuintaCompleta
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi

class QuintaListFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentQuintaListBinding? = null
    private val binding get() = _binding!!
    private var quintas = listOf<Quinta>()
    private var familias = listOf<Familia>()
    private var rondas = listOf<Ronda>()
    private val quintasCompletas = mutableListOf<QuintaCompleta>()
    private val quintasCompletasOriginal = mutableListOf<QuintaCompleta>()
    private var quintasCompletasArg: MutableList<QuintaCompleta>? = null
    private lateinit var quintaAdapter: QuintaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            val data = bundle.getParcelableArrayList<QuintaCompleta>("quintas")
            if(data != null) quintasCompletasArg = data.toMutableList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuintaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrearQuinta.setOnClickListener {
            this.findNavController().navigate(R.id.quintaCreateFragment)
        }

        binding.svQuintas.setOnQueryTextListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        quintaAdapter = QuintaAdapter(quintasCompletas) { quinta: QuintaCompleta ->
            val bundle = bundleOf("quinta" to quinta.toQuinta())
            this.findNavController().navigate(R.id.quintaModifyFragment, bundle)
        }
        binding.rvQuintas.layoutManager = LinearLayoutManager(activity)
        binding.rvQuintas.adapter = quintaAdapter
        listInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listInit() {
        apiCall(suspend {
            quintas = QuintaApi().getQuintas().body()!!
            familias = FamiliaApi().getFamilias().body()!!
            rondas = RondaApi().getRondas().body()!!
        }, {
            quintasCompletas.clear()
            if(quintasCompletasArg != null) quintasCompletas.addAll(quintasCompletasArg!!)
            else quintasCompletas.addAll(quintas.map { quinta ->
                QuintaCompleta.toQuintaCompleta(
                    quinta, familias.find { it.id_fp == quinta.fpId }!!,
                )
            })

            quintasCompletasOriginal.clear()
            quintasCompletasOriginal.addAll(quintasCompletas)

            quintaAdapter.notifyDataSetChanged()
        }, "Hubo un error al actualizar la lista de quintas.")
    }

    private fun filter(query: String?) {
        QuintaCompleta.filter(quintasCompletas, quintasCompletasOriginal, query)
        quintaAdapter.notifyDataSetChanged()
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(query: String?): Boolean {
        filter(query!!.lowercase())
        return true
    }
}