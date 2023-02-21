package app.lajusta.ui.bolson.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentBolsonModifyBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.rondas.Ronda
import app.lajusta.ui.rondas.api.RondaApi

class BolsonModifyFragment: BaseFragment() {

    private var _binding: FragmentBolsonModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var bolson: BolsonCompleto
    private lateinit var verduraBolsonAdapter: VerduraBolsonAdapter
    private var familias = listOf<Familia>()
    private var rondas = listOf<Ronda>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            bolson = bundle.getParcelable("bolson")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBolsonModifyBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()

        binding.bBorrar.setOnClickListener {
            returnSimpleApiCall(
                { BolsonApi().deleteBolson(bolson.id_bolson) },
                "Hubo un error. El bolsón no pudo ser eliminado."
            )
        }

        binding.bGuardar.setOnClickListener {
            // TODO verificar validez de campos

            returnSimpleApiCall(
                { BolsonApi().putBolson(Bolson(
                    bolson.id_bolson,
                    binding.etCantidad.text.toString().toInt(),
                    familias.find {
                        it.nombre == binding.sFamilia.selectedItem.toString()
                    }!!.id_fp,
                    rondas.find {
                        it.id_ronda ==
                            binding.sRonda.selectedItem
                                .toString().split("#")[0].toInt()
                    }!!.id_ronda,
                    verduraBolsonAdapter.getVerduras()
                )) },
                "Hubo un error. El bolson no pudo ser modificado."
            )
        }
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
                binding.sFamilia.setSelection(
                    familiasAdapter.getPosition(bolson.familia.nombre)
                )

                val rondasAdapter = ArrayAdapter(
                    activity!!, R.layout.spinner_item, rondas.map {
                        (it.id_ronda.toString() + "# "
                        + ArrayedDate.toString(it.fecha_inicio)
                        + " - " + ArrayedDate.toString(it.fecha_fin!!))
                    }
                )
                binding.sRonda.adapter = rondasAdapter
                binding.sRonda.setSelection(
                    rondasAdapter.getPosition(
                        (bolson.ronda.id_ronda.toString() + "# "
                        + ArrayedDate.toString(bolson.ronda.fecha_inicio)
                        + " - " + ArrayedDate.toString(bolson.ronda.fecha_fin!!))
                    )
                )
            }, "No se pudieron obtener las familias."
        )
        binding.etCantidad.setText(bolson.cantidad.toString())

        binding.btnAgregarVerdura.setOnClickListener {
            val bundle = bundleOf("idFamilia" to familias.find {
                it.nombre == binding.sFamilia.selectedItem
            }!!.id_fp)
            this.findNavController().navigate(R.id.verduraSelectFragment, bundle)
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        verduraBolsonAdapter = VerduraBolsonAdapter(bolson.verduras)
        binding.rvVerduras.layoutManager = LinearLayoutManager(activity)
        binding.rvVerduras.adapter = verduraBolsonAdapter
    }
}