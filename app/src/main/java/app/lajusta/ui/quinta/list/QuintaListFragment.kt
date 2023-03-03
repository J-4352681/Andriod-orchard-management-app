package app.lajusta.ui.quinta.list

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.data.Preferences.PreferenceHelper.userType
import app.lajusta.databinding.FragmentQuintaListBinding
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.model.QuintaCompleta
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi
import app.lajusta.ui.usuarios.UserRole

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

    private val spName = "User_data"
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = activity?.getSharedPreferences(spName, Context.MODE_PRIVATE)!!
        arguments?.let { bundle ->
            if (bundle.containsKey("quintas"))
                quintasCompletasArg =
                    bundle.getParcelableArrayList<QuintaCompleta>("quintas")!!.toMutableList()
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
            UserRole.getByRoleId(prefs.userType).goToCreationQuinta(findNavController())
        }

        binding.svQuintas.setOnQueryTextListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        quintaAdapter = QuintaAdapter(quintasCompletas) { quinta: QuintaCompleta ->
            UserRole.getByRoleId(prefs.userType).goToModificationQuinta(
                findNavController(), quinta.toQuinta()
            )
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

            actualizarListaUI("No se encontraron quintas en el sistema.")
        }, "Hubo un error al actualizar la lista de quintas.")
    }

    private fun filter(query: String?) {
        QuintaCompleta.filter(quintasCompletas, quintasCompletasOriginal, query)
        actualizarListaUI("No se encontraron quintas que coincidan en el sistema.")
    }

    private fun actualizarListaUI ( emptyMessage:String ) {
        quintasCompletas.sort()
        quintaAdapter.notifyDataSetChanged()
        if (quintasCompletas.isEmpty()) shortToast(emptyMessage)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(query: String?): Boolean {
        filter(query!!.lowercase())
        return true
    }
}