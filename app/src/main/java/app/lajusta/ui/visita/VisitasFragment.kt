package app.lajusta.ui.visita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.lajusta.databinding.FragmentVisitasListBinding

class VisitasFragment : Fragment() {

    private var _binding: FragmentVisitasListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val visitasViewModel =
            ViewModelProvider(this).get(VisitasViewModel::class.java)

        _binding = FragmentVisitasListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}