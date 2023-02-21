package app.lajusta.ui.bolson.create

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentBolsonCreateBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.modify.VerduraBolsonAdapter
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi


class BolsonCreateFragment : BaseFragment() {
    private var _binding: FragmentBolsonCreateBinding? = null
    private val binding get() = _binding!!
    private var cantidad: Int = 0
    private var familias = listOf<Familia>()
    private var rondas = listOf<Ronda>()
    private var verdurasQuinta = listOf<Verdura>()
    private var verdurasNoQuinta = listOf<Verdura>()
    private val verdurasTotales = mutableListOf<Verdura>()
    private lateinit var verduraBolsonAdapter: VerduraBolsonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBolsonCreateBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()
    }

    private fun fillItem() {
        apiCall(
            {
                familias = FamiliaApi().getFamilias().body()!!
                rondas = RondaApi().getRondas().body()!!
            }, {
                val familiasAdapter = ArrayAdapter(
                    activity!!, R.layout.spinner_item, familias.map { it.nombre }
                )
                binding.sFamilia.adapter = familiasAdapter

                val rondasAdapter = ArrayAdapter(
                    activity!!, R.layout.spinner_item, rondas.map {
                        (it.id_ronda.toString() + "# "
                        + ArrayedDate.toString(it.fecha_inicio)
                        + " - " + ArrayedDate.toString(it.fecha_fin!!))
                    }
                )
                binding.sRonda.adapter = rondasAdapter
            }, "No se pudieron obtener las familias."
        )

        initRecyclerView()
    }

    private fun initRecyclerView() {
        verduraBolsonAdapter = VerduraBolsonAdapter(verdurasTotales)
        binding.rvVerduras.layoutManager = LinearLayoutManager(activity)
        binding.rvVerduras.adapter = verduraBolsonAdapter

        val stateHandle: SavedStateHandle =
            findNavController().currentBackStackEntry?.savedStateHandle!!

        stateHandle.getLiveData<List<Verdura>>("verdurasQuinta")
            .observe(viewLifecycleOwner) { data ->
                verdurasQuinta = data
                verdurasTotales.clear()
                verdurasTotales.addAll(verdurasQuinta + verdurasNoQuinta)
            }

        stateHandle.getLiveData<List<Verdura>>("verdurasNoQuinta")
            .observe(viewLifecycleOwner) { data ->
                verdurasNoQuinta = data
                verdurasTotales.clear()
                verdurasTotales.addAll(verdurasQuinta + verdurasNoQuinta)
            }
    }

    private fun setClickListeners() {
        binding.bGuardar.setOnClickListener{
            val cantidadtxt = binding.etCantidad.text.toString()

            if(cantidadtxt.isEmpty()) {
                shortToast("Debe escribir una cantidad")
                return@setOnClickListener
            } else cantidad = cantidadtxt.toInt()

            if(verdurasTotales.size < 7) {
                shortToast("Deberían ser al menos 7 verduras")
                AlertDialog.Builder(activity!!)
                    .setMessage("¿Seguro desea proceder con menos de 7 verduras?")
                    .setCancelable(false)
                    .setPositiveButton("Si") { dialog, id ->
                        commitChange()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else commitChange()
        }

        binding.bCancelar.setOnClickListener{ activity?.onBackPressed() }

        binding.btnAgregarVerdura.setOnClickListener {
            val bundle = bundleOf(
                "idFamilia" to familias.find {
                    it.nombre == binding.sFamilia.selectedItem
                }!!.id_fp,
                "preseleccionadas" to verdurasTotales.map { it.id_verdura }
            )
            this.findNavController().navigate(R.id.verduraSelectFragment, bundle)
        }
    }

    private fun commitChange() {
        returnSimpleApiCall({
            val bolson = Bolson(
                0, cantidad,
                familias.find {
                    it.nombre == binding.sFamilia.selectedItem.toString()
                }!!.id_fp,
                rondas.find {
                    it.id_ronda ==
                            binding.sRonda.selectedItem
                                .toString().split("#")[0].toInt()
                }!!.id_ronda,
                verdurasTotales
            )
            BolsonApi().postBolson(bolson)
        }, "Hubo un error. El bolsón no pudo ser creado.")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}