package app.lajusta.ui.visita.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentVisitasListBinding
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.api.VisitaApi
import app.lajusta.ui.visita.create.VisitasCreateFragment
import app.lajusta.ui.visita.model.VisitaCompleta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VisitasListFragment() : BaseFragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentVisitasListBinding? = null
    private val binding get() = _binding!!
    private val visitasList = mutableListOf<Visita>()
    private val quintasList = mutableListOf<Quinta>()
    private val tecnicosList = mutableListOf<Usuario>()
    private val visitasCompletasList = mutableListOf<VisitaCompleta>()
    private lateinit var visitaAdapter: VisitaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitasListBinding.inflate(inflater, container, false)

        binding.svVisitas.setOnQueryTextListener(this)
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddVisita.setOnClickListener{
            view.findNavController().navigate(R.id.visitasCreateFragment)
        }
    }

    private fun initRecyclerView() {
        visitaAdapter= VisitaAdapter(visitasCompletasList) { visita: VisitaCompleta ->
            val bundle = bundleOf("visita" to visita)
            this.findNavController().navigate(
                R.id.action_nav_visita_to_visitaModifyFragment, bundle
            )
        }
        binding.rvVisitas.layoutManager = LinearLayoutManager(activity)
        binding.rvVisitas.adapter = visitaAdapter
        filter("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filter(query: String) {
        apiCall(suspend {
            val visitas = VisitaApi().getVisitas().body()!!
            val quintas = QuintaApi().getQuintas().body()!!
            val tecnicos = UsuariosApi().getUsuarios().body()!!

            activity!!.runOnUiThread {
                visitasList.clear()
                visitasList.addAll(visitas)

                quintasList.clear()
                quintasList.addAll(quintas)

                tecnicosList.clear()
                tecnicosList.addAll(tecnicos)

                fillVisitasCompletas()
            }
        }, "Hubo un error al actualizar la lista de visitas.")
    }

    private fun fillVisitasCompletas(){
        visitasCompletasList.clear()
        visitasList.forEach { visita ->
            visitasCompletasList.add(
                VisitaCompleta.toVisitaCompleta(
                    visita,
                    tecnicosList.find { it.id_user == visita.id_tecnico }!!,
                    quintasList.find { it.id_quinta == visita.id_quinta }!!
                )
            )
        }
        visitaAdapter.notifyDataSetChanged()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()) filter(query.lowercase())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}