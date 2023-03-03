package app.lajusta.ui.visita.list

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
import app.lajusta.data.Preferences.PreferenceHelper.userId
import app.lajusta.data.Preferences.PreferenceHelper.userType
import app.lajusta.databinding.FragmentVisitasListBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.UserRole
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.api.VisitaApi
import app.lajusta.ui.visita.model.VisitaCompleta
import java.time.LocalDate

class VisitasListFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentVisitasListBinding? = null
    private val binding get() = _binding!!
    private var visitas = listOf<Visita>()
    private var quintas = listOf<Quinta>()
    private var tecnicos = listOf<Usuario>()
    private val visitasCompletas = mutableListOf<VisitaCompleta>()
    private val visitasCompletasOriginal = mutableListOf<VisitaCompleta>()
    private var visitasCompletasArg: MutableList<VisitaCompleta>? = null
    private lateinit var visitaAdapter: VisitaAdapter

    private val spName = "User_data"
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = activity?.getSharedPreferences(spName, Context.MODE_PRIVATE)!!
        arguments?.let { bundle ->
            val data = bundle.getParcelableArrayList<VisitaCompleta>("visitas")
            if(data != null) visitasCompletasArg = data.toMutableList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitasListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddVisita.setOnClickListener {
            UserRole.getByRoleId(prefs.userType).goToVisitaCreation(
                prefs.userId, findNavController()
            )
        }

        binding.svVisitas.setOnQueryTextListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        visitaAdapter= VisitaAdapter(visitasCompletas) { visita: VisitaCompleta ->
            if (ArrayedDate.toLocalDate(visita.fecha_visita)
                    .isBefore(LocalDate.now().minusMonths(1))
            ) {
                val bundle = bundleOf("visita" to visita.toVisita().toBlockedPrefilledVisita())
                findNavController().navigate(R.id.visitaModifyFragment, bundle)
            }
            else {UserRole.getByRoleId(prefs.userType).goToModificationVisita(
                prefs.userId, visita.toVisita(), findNavController()
            )}
        }
        binding.rvVisitas.layoutManager = LinearLayoutManager(activity)
        binding.rvVisitas.adapter = visitaAdapter
        initList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initList() {
        apiCall(suspend {
            visitas = VisitaApi().getVisitas().body()!!
            quintas = QuintaApi().getQuintas().body()!!
            tecnicos = UsuariosApi().getUsuarios().body()!!
        }, {
            visitasCompletas.clear()
            if(visitasCompletasArg != null) visitasCompletas.addAll(visitasCompletasArg!!)
            else visitasCompletas.addAll(visitas.map { visita ->
                VisitaCompleta.toVisitaCompleta(
                    visita,
                    tecnicos.find { it.id_user == visita.id_tecnico }!!,
                    quintas.find { it.id_quinta == visita.id_quinta }!!
                )
            })

            visitasCompletasOriginal.clear()
            visitasCompletasOriginal.addAll(visitasCompletas)

            actualizarListaUI("No se encontraron visitas en el sistema")
        }, "Hubo un error al actualizar la lista de visitas.")
    }

    private fun filter(query: String?) {
        VisitaCompleta.filter(visitasCompletas, visitasCompletasOriginal, query)
        actualizarListaUI("No se encontraron visitas que coincidan en el sistema.")
    }

    private fun actualizarListaUI ( emptyMessage:String ) {
        visitasCompletas.sortDescending()
        visitaAdapter.notifyDataSetChanged()
        if (visitasCompletas.isEmpty()) shortToast(emptyMessage)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(query: String?): Boolean {
        filter(query!!.lowercase())
        return true
    }
}