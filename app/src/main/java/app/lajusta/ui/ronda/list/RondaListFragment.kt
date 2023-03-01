package app.lajusta.ui.ronda.list

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
import app.lajusta.databinding.FragmentRondaListBinding
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi
import app.lajusta.ui.usuarios.UserRole

class RondaListFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentRondaListBinding? = null
    private val binding get() = _binding!!
    private var rondasApi = listOf<Ronda>()
    private val rondas = mutableListOf<Ronda>()
    private val rondasOriginal = mutableListOf<Ronda>()
    private var rondasArg: MutableList<Ronda>? = null
    private lateinit var rondaAdapter: RondaAdapter

    private val spName = "User_data"
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = activity?.getSharedPreferences(spName, Context.MODE_PRIVATE)!!
        arguments?.let { bundle ->
            val data = bundle.getParcelableArrayList<Ronda>("rondas")
            if(data != null) rondasArg = data.toMutableList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRondaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrearRonda.setOnClickListener {
            UserRole.getByRoleId(prefs.userType).goToCreationRonda(findNavController())
        }

        binding.svRondas.setOnQueryTextListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        rondaAdapter = RondaAdapter(rondas) { ronda: Ronda ->
            if(ronda.isActive())
                UserRole.getByRoleId(prefs.userType)
                    .goToModificationRonda(findNavController(), ronda)
            else findNavController().navigate(R.id.rondaModifyFragment, bundleOf(
                "prefilledRonda" to ronda.toBlockedPrefilledRonda()
            ))
        }
        binding.rvRondas.layoutManager = LinearLayoutManager(activity)
        binding.rvRondas.adapter = rondaAdapter
        listInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listInit() {
        apiCall(suspend {
            rondasApi = RondaApi().getRondas().body()!!
        }, {
            rondas.clear()
            if(rondasArg != null) rondas.addAll(rondasArg!!)
            else rondas.addAll(rondasApi)

            rondasOriginal.clear()
            rondasOriginal.addAll(rondas)

            rondas.sortDescending()

            rondaAdapter.notifyDataSetChanged()
        }, "Hubo un error al actualizar la lista de rondas.")
    }

    private fun filter(query: String?) {
        Ronda.filter(rondas, rondasOriginal, query)
        rondas.sortDescending()
        rondaAdapter.notifyDataSetChanged()
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(query: String?): Boolean {
        filter(query!!.lowercase())
        return true
    }
}