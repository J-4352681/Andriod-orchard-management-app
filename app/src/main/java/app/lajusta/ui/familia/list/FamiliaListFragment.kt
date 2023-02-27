package app.lajusta.ui.familia.list

import android.content.Context
import android.content.SharedPreferences
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
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.FamiliaCompleta
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi

class FamiliaListFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentFamiliasListBinding? = null
    private val binding get() = _binding!!
    private var familias = listOf<Familia>()
    private var quintas = listOf<Quinta>()
    private var bolsones = listOf<Bolson>()
    private var rondas = listOf<Ronda>()
    private val familiasCompletas = mutableListOf<FamiliaCompleta>()
    private val familiasCompletasOriginal = mutableListOf<FamiliaCompleta>()
    private var familiasCompletasArg: MutableList<FamiliaCompleta>? = null
    private lateinit var familiaAdapter: FamiliaAdapter

    private val spName = "User_data"
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = activity?.getSharedPreferences(spName, Context.MODE_PRIVATE)!!
        arguments?.let { bundle ->
            val data = bundle.getParcelableArrayList<FamiliaCompleta>("familias")
            if(data != null) familiasCompletasArg = data?.toMutableList()!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFamiliasListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrearFamilia.setOnClickListener{
            view.findNavController().navigate(R.id.familiaCreateFragment)
        }

        binding.svFamilia.setOnQueryTextListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        familiaAdapter = FamiliaAdapter(familiasCompletas) { familia: FamiliaCompleta ->
            val bundle = bundleOf("familia" to familia)
            this.findNavController().navigate(R.id.familiaModifyFragment, bundle)
        }
        binding.rvFamilias.layoutManager = LinearLayoutManager(activity)
        binding.rvFamilias.adapter = familiaAdapter
        initList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initList() {
        apiCall(suspend {
            familias = FamiliaApi().getFamilias().body()!!
            quintas = QuintaApi().getQuintas().body()!!
            bolsones = BolsonApi().getBolsones().body()!!
            rondas = RondaApi().getRondas().body()!!
        }, {
            familiasCompletas.clear()
            if(familiasCompletasArg != null) familiasCompletas.addAll(familiasCompletasArg!!)
            else familiasCompletas.addAll(familias.map { familia ->
                val quintas = quintas.filter { it.fpId == familia.id_fp }.toMutableList()
                val bolsones = bolsones.filter { it.idFp == familia.id_fp }.toMutableList()
                val rondas = rondas.filter { ronda ->
                    bolsones.any { bolson -> bolson.idRonda == ronda.id_ronda }
                }.toMutableList()

                FamiliaCompleta.toFamiliaCompleta(familia, quintas, bolsones, rondas)
            })

            familiasCompletasOriginal.clear()
            familiasCompletasOriginal.addAll(familiasCompletas)

            familiaAdapter.notifyDataSetChanged()
        }, "Hubo un error al listar las familias.")
    }

    private fun filter(query: String) {
        FamiliaCompleta.filter(familiasCompletas, familiasCompletasOriginal, query)
        familiaAdapter.notifyDataSetChanged()
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(query: String?): Boolean {
        filter(query!!.lowercase())
        return true
    }
}