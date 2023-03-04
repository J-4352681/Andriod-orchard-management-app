package app.lajusta.ui.bolson.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentVerduraSelectBinding
import app.lajusta.ui.bolson.Bolson
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
    private lateinit var bolson: Bolson

    private var quintas = listOf<Quinta>()
    private var visitas = listOf<Visita>()
    private var verduras = listOf<Verdura>()

    private val verdurasQuinta = mutableListOf<Verdura>()
    private val verdurasQuintaSeleccionadas = mutableListOf<Verdura>()
    private lateinit var verdurasQuintaAdapter: VerduraSelectAdapter

    private val verdurasNoQuinta = mutableListOf<Verdura>()
    private val verdurasNoQuintaSeleccionadas = mutableListOf<Verdura>()
    private lateinit var verdurasNoQuintaAdapter: VerduraSelectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            bolson = bundle.getParcelable("bolson")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerduraSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()
    }


    private fun setClickListeners() {
        binding.bAceptar.setOnClickListener {
            val navController = this.findNavController()

            val stateHandler: SavedStateHandle =
                navController.previousBackStackEntry?.savedStateHandle!!
            bolson.verduras.clear()
            bolson.verduras.addAll(verdurasQuintaSeleccionadas + verdurasNoQuintaSeleccionadas)
            stateHandler["bolson"] = bolson

            navController.popBackStack()
        }
    }


    private fun fillItem() {
        apiCallProgressBar(
            {
                verduras = VerduraApi().getVerduras().body()!!
                quintas = QuintaApi().getQuintas().body()!!
                visitas = VisitaApi().getVisitas().body()!!
            }, {
                quintas = quintas.filter { it.fpId == bolson.idFp }
                visitas = visitas
                    .filter { quintas.map { it.id_quinta }.contains(it.id_quinta) }
                    .map { visita ->
                        visitas
                            .filter { it.id_visita == visita.id_visita }
                            .maxBy { ArrayedDate.toDate(it.fecha_visita) }
                    }.toSet().toList()

                // TODO filtrar visitas anteriores a hace 6 meses

                verdurasQuinta.addAll(verduras.filter {
                    visitas.map {
                        it.parcelas.map { it.verdura.id_verdura }
                    }.flatten().contains(it.id_verdura)
                })
                verdurasNoQuinta.addAll(verduras - verdurasQuinta.toSet())

                verdurasQuintaAdapter.notifyDataSetChanged()
                verdurasNoQuintaAdapter.notifyDataSetChanged()

                verdurasQuintaSeleccionadas.addAll(
                    verdurasQuinta
                        .filter {
                            bolson.verduras.map { it.id_verdura }.contains(it.id_verdura)
                        }
                )
                verdurasNoQuintaSeleccionadas.addAll(
                    verdurasNoQuinta.filter {
                        bolson.verduras.map { it.id_verdura }.contains(it.id_verdura)
                    }
                )
            }, "Hubo un problema obteniendo las verduras", binding.progressBar
        )

        initRecyclerViewQuinta()
        initRecyclerViewNoQuinta()
    }

    private fun initRecyclerViewQuinta() {
        verdurasQuintaAdapter =
            VerduraSelectAdapter(verdurasQuinta, bolson.verduras.map { it.id_verdura }, { cb ->
                if (verdurasQuintaSeleccionadas.size == 5)
                    shortToast("Deberían elegirse al menos 5 verduras de la quinta.")

                if (verdurasQuintaSeleccionadas.size + verdurasNoQuintaSeleccionadas.size == 7)
                    shortToast("Deberían elegirse al menos 7 verduras en total.")

                verdurasQuintaSeleccionadas.remove(
                    verdurasQuinta.find { it.nombre == cb.text.toString() }
                )
            }, { cb ->
                verdurasQuintaSeleccionadas.add(
                    verdurasQuinta.find { it.nombre == cb.text.toString() }!!
                )
            })
        binding.rvQuinta.layoutManager = LinearLayoutManager(activity)
        binding.rvQuinta.adapter = verdurasQuintaAdapter
    }

    private fun initRecyclerViewNoQuinta() {
        verdurasNoQuintaAdapter =
            VerduraSelectAdapter(verdurasNoQuinta, bolson.verduras.map { it.id_verdura }, { cb ->
                if (verdurasQuintaSeleccionadas.size + verdurasNoQuintaSeleccionadas.size == 7)
                    shortToast("Deberían elegirse al menos 7 verduras en total.")

                verdurasNoQuintaSeleccionadas.remove(
                    verdurasNoQuinta.find { it.nombre == cb.text.toString() }
                )
            }, { cb ->
                verdurasNoQuintaSeleccionadas.add(
                    verdurasNoQuinta.find { it.nombre == cb.text.toString() }!!
                )

                if (verdurasNoQuintaSeleccionadas.size == 2)
                    shortToast("Deberían elegirse a lo sumo 2 verduras que no sea de la quinta.")
            })
        binding.rvNoQuinta.layoutManager = LinearLayoutManager(activity)
        binding.rvNoQuinta.adapter = verdurasNoQuintaAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}