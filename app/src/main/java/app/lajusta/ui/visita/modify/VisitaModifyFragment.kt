package app.lajusta.ui.visita.modify

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.databinding.FragmentVisitaModifyBinding
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.api.VisitaApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VisitaModifyFragment() : BaseFragment() {
    private var _binding: FragmentVisitaModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var visita: Visita
    private lateinit var parcelaVisitaAdapter: ParcelaVisitaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            visita = bundle.getParcelable("visita")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitaModifyBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    private var quintas_encontradas:List<Quinta>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()

        binding.bBorrar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try { VisitaApi().deleteVisita(visita.id_visita) }
                catch (e: Exception) { activity!!.runOnUiThread { shortToast(
                    "Hubo un error. El elemento no pudo ser eliminado."
                ) } }
                finally { activity!!.runOnUiThread { activity!!.onBackPressed() } }
            }
        }

        binding.bGuardar.setOnClickListener {
            try {
                visita.id_tecnico = binding.etTecnico.text.toString().toInt()
                //visita.fecha_visita = binding.etFecha.text.toString().toDate()
                visita.descripcion = binding.etDesc.text.toString()
                visita.id_quinta = binding.spinnerQuinta.id
                visita.parcelas = parcelaVisitaAdapter.getParcelas()

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = VisitaApi().putVisita(visita)
                        activity!!.runOnUiThread { longToast(
                            response.body().toString()
                        ) }
                        if(!response.isSuccessful) throw Exception(response.code().toString())
                    }
                    catch(e: Exception) { activity!!.runOnUiThread { shortToast(
                        "Hubo un error. El elemento no pudo ser modificado."
                    ) } }
                    finally { activity!!.runOnUiThread { activity!!.onBackPressed() } }
                }
            } catch(e: Exception) {
                shortToast("Complete los campos con valores validos según corresponda.")
            }
        }
    }

    private fun fillItem() {
        binding.tvTitle.text = "Modificando visita " + visita.id_visita.toString()
        binding.etFecha.setText(visita.fecha_visita.toString())
        binding.etTecnico.setText(visita.id_tecnico.toString())
        binding.etDesc.setText(visita.descripcion.toString())

        //LLenar combobox y iniciar veriable quintas_encontradas
        CoroutineScope(Dispatchers.IO).launch {
            lateinit var quintas: List<Quinta>
            try {
                quintas = QuintaApi().getQuintas().body()!!
                activity!!.runOnUiThread {

                    // access the items of the list
                    val nombresQuintas: Array<String> = getNombreQuintas(quintas)

                    // access the spinner
                    val spinner = binding.spinnerQuinta
                    val adapter: ArrayAdapter<String>? = (activity?.let {
                        ArrayAdapter<String>(
                            it,
                            R.layout.simple_spinner_item, nombresQuintas
                        )
                    })

                    spinner.adapter = adapter

                }

                quintas_encontradas = quintas

            } catch(e: Exception) {
                activity!!.runOnUiThread {
                    shortToast("Hubo un error al listar las quintas.")
                }
            }
        }

        //TODO COMPLETAR CAMPOS DE LA VISITA CON LOS DATOS


        initRecyclerView()
    }

    private fun getNombreQuintas(quintas:List<Quinta>):Array<String>{
        return quintas.map { it.nombre.orEmpty() }.toTypedArray()
    }

    private fun initRecyclerView() {
        parcelaVisitaAdapter = ParcelaVisitaAdapter(visita.parcelas)
        binding.rvParcelas.layoutManager = LinearLayoutManager(activity)
        binding.rvParcelas.adapter = parcelaVisitaAdapter
    }
}