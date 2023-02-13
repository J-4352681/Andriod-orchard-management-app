package app.lajusta.ui.bolson.modify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentBolsonModifyBinding
import app.lajusta.ui.bolson.Bolson

class BolsonModifyFragment() : Fragment() {

    private var _binding: FragmentBolsonModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var bolson: Bolson

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
        _binding = FragmentBolsonModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
    }

    private fun fillItem() {
        binding.etCantidad.setText(bolson.cantidad.toString())
        binding.etId.setText(bolson.id_bolson.toString())
        binding.etFamilia.setText(bolson.idFp.toString())
        binding.etRonda.setText(bolson.idRonda.toString())
    }
}