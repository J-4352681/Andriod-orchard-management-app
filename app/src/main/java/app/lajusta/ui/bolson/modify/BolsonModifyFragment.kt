package app.lajusta.ui.bolson.modify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.lajusta.databinding.FragmentBolsonModifyBinding
import app.lajusta.ui.bolson.Bolson

class BolsonModifyFragment(
    private val bolson: Bolson
) : Fragment() {

    private var _binding: FragmentBolsonModifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBolsonModifyBinding.inflate(inflater, container, false)
        fillItem()

        return binding.root
    }

    private fun fillItem() {
        binding.etCantidad.setText(bolson.cantidad.toString())
        binding.etId.setText(bolson.id_bolson.toString())
        binding.etFamilia.setText(bolson.idFp.toString())
        binding.etRonda.setText(bolson.idRonda.toString())
    }
}