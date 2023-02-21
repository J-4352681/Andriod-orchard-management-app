package app.lajusta.ui.bolson.extra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentVerduraSelectBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.verdura.api.VerduraApi
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.api.VisitaApi

class VerduraSelectFragment : BaseFragment() {
    private var _binding: FragmentVerduraSelectBinding? = null
    private val binding get() = _binding!!
    private var idFamilia: Int = -1
    private var quintas = listOf<Quinta>()
    private var visitas = listOf<Visita>()
    private var verduras = listOf<Verdura>()
    private val verdurasQuinta = mutableListOf<Verdura>()
    private val verdurasNoQuinta = mutableListOf<Verdura>()
    private val verdurasQuintaSeleccionadas = mutableListOf<String>()
    private val verdurasNoQuintaSeleccionadas = mutableListOf<String>()
    private lateinit var verdurasQuintaAdapter: VerduraSelectAdapter
    private lateinit var verdurasNoQuintaAdapter: VerduraSelectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idFamilia = arguments?.getInt("idFamilia")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerduraSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listInit()
        initRecyclerView()
        setClickListeners()
    }

    private fun initRecyclerView() {
        verdurasQuintaAdapter = VerduraSelectAdapter(verdurasQuinta) { cb ->
            if (cb.isChecked) {
                if (verdurasQuintaSeleccionadas.size == 5)
                    shortToast("Deberían elegirse al menos 5 verduras de la quinta.")

                if (verdurasQuintaSeleccionadas.size + verdurasNoQuintaSeleccionadas.size == 7)
                    shortToast("Deberían elegirse al menos 7 verduras en total.")

                verdurasQuintaSeleccionadas.remove(cb.text.toString())
            } else verdurasQuintaSeleccionadas.add(cb.text.toString())

            cb.isChecked = !cb.isChecked
        }
        binding.rvQuinta.layoutManager = LinearLayoutManager(activity)
        binding.rvQuinta.adapter = verdurasQuintaAdapter

        verdurasNoQuintaAdapter = VerduraSelectAdapter(verdurasNoQuinta) { cb ->
            if (cb.isChecked) {
                if (verdurasQuintaSeleccionadas.size + verdurasNoQuintaSeleccionadas.size == 7)
                    shortToast("Deberían elegirse al menos 7 verduras en total.")

                verdurasNoQuintaSeleccionadas.remove(cb.text.toString())
            } else {
                verdurasNoQuintaSeleccionadas.add(cb.text.toString())

                if (verdurasNoQuintaSeleccionadas.size == 2)
                    shortToast("Deberían elegirse a lo sumo 2 verduras que no sea de la quinta.")
            }

            cb.isChecked = !cb.isChecked
        }
        binding.rvNoQuinta.layoutManager = LinearLayoutManager(activity)
        binding.rvNoQuinta.adapter = verdurasNoQuintaAdapter

        println("J")
    }

    private fun setClickListeners() {
        binding.bAceptar.setOnClickListener {
            println(verdurasQuintaSeleccionadas)
            println(verdurasNoQuintaSeleccionadas)
            val navController = this.findNavController()

            val stateHandler: SavedStateHandle =
                navController.previousBackStackEntry?.savedStateHandle!!
            stateHandler["verdurasQuinta"] = verdurasQuinta.filter {
                verdurasQuintaSeleccionadas.contains(it.nombre)
            }
            stateHandler["verdurasNoQuinta"] = verdurasNoQuinta.filter {
                verdurasNoQuintaSeleccionadas.contains(it.nombre)
            }
            println(verdurasQuintaSeleccionadas)

            navController.popBackStack()
        }
    }

    private fun listInit() {
        apiCall(
            {
                verduras = VerduraApi().getVerduras().body()!!
                quintas = QuintaApi().getQuintas().body()!!
                visitas = VisitaApi().getVisitas().body()!!
            }, {
                quintas = quintas.filter { it.fpId == idFamilia }
                visitas = visitas
                    .filter { quintas.map { it.id_quinta }.contains(it.id_quinta) }
                    .map { visita ->
                        visitas
                            .filter { it.id_visita == visita.id_visita }
                            .maxBy { ArrayedDate.toDate(it.fecha_visita) }
                    }.toSet().toList()

                // TODO filtrar visitas anteriores a hace 6 meses

                verdurasQuinta.clear()
                verdurasQuinta.addAll(verduras.filter {
                    visitas.map {
                        it.parcelas.map { it.verdura.id_verdura }
                    }.flatten().contains(it.id_verdura)
                })
                verdurasNoQuinta.clear()
                verdurasNoQuinta.addAll(verduras - verdurasQuinta.toSet())

                verdurasQuintaAdapter.notifyDataSetChanged()
                verdurasNoQuintaAdapter.notifyDataSetChanged()
            }, "Hubo un problema obteniendo las verduras"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}