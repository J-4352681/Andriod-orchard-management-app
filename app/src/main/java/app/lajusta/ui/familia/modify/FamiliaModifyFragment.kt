package app.lajusta.ui.familia.modify

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import app.lajusta.R
import app.lajusta.data.Preferences.PreferenceHelper.userType
import app.lajusta.databinding.FragmentFamiliaModifyBinding
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.familia.FamiliaCompleta
import app.lajusta.ui.familia.PrefilledFamilia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.login.afterTextChanged
import app.lajusta.ui.quinta.PrefilledQuinta
import app.lajusta.ui.quinta.model.QuintaCompleta
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.ronda.api.RondaApi
import app.lajusta.ui.usuarios.UserRole

class FamiliaModifyFragment: BaseFragment(){

    private var _binding: FragmentFamiliaModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var familia: FamiliaCompleta
    private val bolsonesCompletos = mutableListOf<BolsonCompleto>()
    private val quintasCompletas = mutableListOf<QuintaCompleta>()
    private var rondas = listOf<Ronda>()
    private var prefilledFamilia: PrefilledFamilia? = null

    private val spName = "User_data"
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = activity?.getSharedPreferences(spName, Context.MODE_PRIVATE)!!
        arguments?.let { bundle ->
            if(bundle.containsKey("familia"))
                familia = bundle.getParcelable("familia")!!
            if(bundle.containsKey("prefilledFamilia"))
                prefilledFamilia = bundle.getParcelable("prefilledFamilia")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamiliaModifyBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()

        binding.etNombre.afterTextChanged { nombre -> familia.nombre = nombre }
    }

    private fun fillItem() {
        binding.tvFechaSeleccionada.text = ArrayedDate.toString(familia.fecha_afiliacion)

        binding.tvTitle.text =
            ("Familia " + familia.nombre
            + " afiliada " + ArrayedDate.toString(familia.fecha_afiliacion))

        binding.etNombre.setText(familia.nombre)

        apiCallProgressBar(suspend {
            rondas = RondaApi().getRondas().body()!!.toMutableList()
        }, {
            fillQuintas()
            fillRondas()
            setClickListeners()
            prefillFamilia()
        }, "No se pudieron obtener las quintas y/o bolsones de la familia.", binding.progressBar)
    }

    private fun fillQuintas() {
        quintasCompletas.clear()
        quintasCompletas.addAll(familia.quintas.map { quinta ->
            QuintaCompleta.toQuintaCompleta(quinta, familia.toFamilia())
        })

        val textQuintas = familia.quintas.map { it.nombre }.toString()
        binding.tvQuintasList.text = textQuintas.subSequence(1, textQuintas.length-1)
    }

    private fun fillRondas() {
        bolsonesCompletos.clear()
        bolsonesCompletos.addAll(familia.bolsones.map { bolson ->
            BolsonCompleto.toBolsonCompleto(bolson, familia.toFamilia(), familia.rondas.find {
                it.id_ronda == bolson.idRonda
            }!!)
        })

        val fechasBolsonesRondas = bolsonesCompletos.map {
            ArrayedDate.toString(it.ronda.fecha_inicio.toMutableList())
        }.toString()
        binding.tvUltimoBolson.text = fechasBolsonesRondas
            .subSequence(1, fechasBolsonesRondas.length-1)
    }

    private fun setClickListeners() {
        binding.bFecha.setOnClickListener(
            ArrayedDate.datePickerListener(
                activity!!, binding.tvFechaSeleccionada
            ) { _, i, i2, i3 ->
                familia.fecha_afiliacion = listOf(i, i2+1, i3)
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(familia.fecha_afiliacion)
            }
        )

        binding.bBorrar.setOnClickListener {
            returnSimpleApiCall(
                { FamiliaApi().deleteFamilia(familia.id_fp) },
                "Hubo un error. La familia no pudo ser eliminada."
            )
        }

        binding.bGuardar.setOnClickListener {
            if(ArrayedDate.laterThanToday(ArrayedDate.toString(familia.fecha_afiliacion))) {
                shortToast("La fecha de afiliación no puede ser posterior a la actual")
                return@setOnClickListener
            }

            if(familia.nombre.isEmpty()) {
                shortToast("El nombre de la familia no puede quedar vacío")
                return@setOnClickListener
            }

            returnSimpleApiCall(
                { FamiliaApi().putFamilia(familia.toFamilia()) },
                "Hubo un error. La familia no pudo ser modificado."
            )
        }

        binding.bVerBolsones.setOnClickListener {
            val bundle = bundleOf("bolsones" to ArrayList<BolsonCompleto>(bolsonesCompletos))
            this.findNavController().navigate(
                R.id.bolsonFilteredListFragment, bundle
            )
        }

        binding.bVerQuintas.setOnClickListener {
            val bundle = bundleOf("quintas" to ArrayList<QuintaCompleta>(quintasCompletas))
            this.findNavController().navigate(
                R.id.quintaFilteredListFragment, bundle
            )
        }

        binding.bNuevaQuinta.setOnClickListener {
            UserRole.getByRoleId(prefs.userType).goToCreationQuinta(
                findNavController(), PrefilledQuinta(
                    fpId = familia.id_fp, _blockFields = true
                )
            )
        }

        binding.bVerRondas.setOnClickListener {
            val bundle = bundleOf("rondas" to ArrayList<Ronda>(familia.rondas))
            this.findNavController().navigate(
                R.id.rondaFilteredListFragment, bundle
            )
        }
    }

    private fun prefillFamilia() {
        if(prefilledFamilia != null) {
            if(prefilledFamilia?.nombre != null) {
                familia.nombre = prefilledFamilia?.nombre!!
                binding.etNombre.setText(familia.nombre)
                if(prefilledFamilia?._blockFields!!) binding.etNombre.isEnabled = false
            }
            if(prefilledFamilia?.fecha_afiliacion != null) {
                familia.fecha_afiliacion = prefilledFamilia?.fecha_afiliacion!!
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(familia.fecha_afiliacion)
                if(prefilledFamilia?._blockFields!!) binding.bFecha.isEnabled = false
            }
            if(prefilledFamilia?._blockSubmitAction!!) {
                binding.bGuardar.isEnabled = false
                binding.bBorrar.isEnabled = false
            }
            if(prefilledFamilia?._blockFields!!) {
                binding.bNuevaQuinta.isEnabled = false
                binding.bVerRondas.isEnabled = false
            }
        }
    }
}