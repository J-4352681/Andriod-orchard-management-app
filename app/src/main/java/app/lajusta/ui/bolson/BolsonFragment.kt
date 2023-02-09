package app.lajusta.ui.bolson

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.lajusta.databinding.FragmentBolsonBinding

class BolsonFragment : Fragment() {

    private var _binding: FragmentBolsonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bolsonViewModel =
            ViewModelProvider(this).get(BolsonViewModel::class.java)

        _binding = FragmentBolsonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textBolson
        bolsonViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}