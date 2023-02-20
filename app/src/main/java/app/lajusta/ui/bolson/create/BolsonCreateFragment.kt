package app.lajusta.ui.bolson.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import app.lajusta.R
import app.lajusta.databinding.FragmentBolsonCreateBinding
import app.lajusta.databinding.FragmentBolsonModifyBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.rondas.Ronda
import app.lajusta.ui.rondas.api.RondaApi
import kotlin.random.Random


class BolsonCreateFragment : BaseFragment() {
    private var _binding: FragmentBolsonCreateBinding? = null
    private val binding get() = _binding!!
    private var familias = listOf<Familia>()
    private var rondas = listOf<Ronda>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBolsonCreateBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    private val verdura: Verdura = Verdura(1, arrayOf(2, 3, 4), arrayOf(1, 2, 4),"dsadfs", "Tomate","fssd")
    private val verduras: List<Verdura> = listOf(verdura,verdura,verdura,verdura,verdura,verdura,verdura) /** CAMBIAR POR EL CRUD DE VERDURAS */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()

        binding.bGuardar.setOnClickListener{
            val cantidad = binding.etCantidad.text.toString()

            if(cantidad.isNullOrEmpty()) {
                shortToast("Debe escribir una cantidad")
                return@setOnClickListener
            }

            if(verduras.size < 7) {
                shortToast("Deben ser al menos 7 verduras")
                return@setOnClickListener
            }

            returnSimpleApiCall({
                BolsonApi().postBolson(Bolson(
                    0, cantidad.toInt(),
                    familias.find {
                        it.nombre == binding.sFamilia.selectedItem.toString()
                    }!!.id_fp,
                    rondas.find {
                        it.id_ronda ==
                            binding.sRonda.selectedItem
                                .toString().split("#")[0].toInt()
                    }!!.id_ronda,
                    verduras
                ) )
            }, "Hubo un error. El bolsón no pudo ser creado.")
        }

        binding.bCancelar.setOnClickListener{ activity?.onBackPressed() }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}