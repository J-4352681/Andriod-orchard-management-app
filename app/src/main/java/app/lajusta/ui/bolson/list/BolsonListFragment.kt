package app.lajusta.ui.bolson.list

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.data.Preferences.PreferenceHelper.userType
import app.lajusta.databinding.FragmentBolsonListBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi
import app.lajusta.ui.usuarios.UserRole

class BolsonListFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentBolsonListBinding? = null
    private val binding get() = _binding!!
    private var bolsones = listOf<Bolson>()
    private var familias = listOf<Familia>()
    private var rondas = listOf<Ronda>()
    private val bolsonesCompletos = mutableListOf<BolsonCompleto>()
    private val bolsonesCompletosOriginal = mutableListOf<BolsonCompleto>()
    private var bolsonesCompletosArg: MutableList<BolsonCompleto>? = null
    private lateinit var bolsonAdapter: BolsonAdapter

    private val spName = "User_data"
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = activity?.getSharedPreferences(spName, Context.MODE_PRIVATE)!!
        arguments?.let { bundle ->
            val data = bundle.getParcelableArrayList<BolsonCompleto>("bolsones")
            if(data != null) bolsonesCompletosArg = data.toMutableList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBolsonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listInit()

        binding.svBolsones.setOnQueryTextListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        bolsonAdapter = BolsonAdapter(bolsonesCompletos) { bolson: Bolson ->
            val ronda = rondas.find { it.id_ronda == bolson.idRonda }
            if ((ronda?.fecha_fin == null) || !ArrayedDate.laterThanToday(
                    ArrayedDate.toString(ronda.fecha_fin!!)
                )
            ) UserRole.getByRoleId(prefs.userType).goToModificationBolson(
                findNavController(), bolson
            ) else {
                val bundle = bundleOf("prefilledBolson" to bolson.toBlockedPrefilledBolson())
                findNavController().navigate(R.id.bolsonModifyFragment, bundle)
            }
        }
        binding.rvBolsones.layoutManager = LinearLayoutManager(activity)
        binding.rvBolsones.adapter = bolsonAdapter
    }

    private fun listInit() {
        apiCall(suspend {
            bolsones = BolsonApi().getBolsones().body()!!
            familias = FamiliaApi().getFamilias().body()!!
            rondas = RondaApi().getRondas().body()!!
        }, {
            bolsonesCompletos.clear()
            if(bolsonesCompletosArg != null) bolsonesCompletos.addAll(bolsonesCompletosArg!!)
            else bolsonesCompletos.addAll(bolsones.map { bolson ->
                BolsonCompleto.toBolsonCompleto(
                    bolson,
                    familias.find { it.id_fp == bolson.idFp }!!,
                    rondas.find { it.id_ronda == bolson.idRonda }!!
                )
            })

            bolsonesCompletosOriginal.clear()
            bolsonesCompletosOriginal.addAll(bolsonesCompletos)

            bolsonesCompletos.sortDescending()

            bolsonAdapter.notifyDataSetChanged()

            setClickListeners()
        }, "Hubo un error al actualizar la lista de bolsones.")
    }

    private fun setClickListeners() {
        binding.fabCrearBolson.setOnClickListener {
            UserRole.getByRoleId(prefs.userType).goToCreationBolson(findNavController())
        }
    }

    private fun filter(query: String?) {
        BolsonCompleto.filter(bolsonesCompletos, bolsonesCompletosOriginal, query)

        bolsonesCompletos.sortDescending()

        bolsonAdapter.notifyDataSetChanged()
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(query: String?): Boolean {
        filter(query!!.lowercase())
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}