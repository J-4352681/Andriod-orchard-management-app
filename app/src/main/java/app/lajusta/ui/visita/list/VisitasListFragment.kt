package app.lajusta.ui.visita.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import app.lajusta.R
import app.lajusta.databinding.FragmentVisitasListBinding

class VisitasListFragment : Fragment() {

    private var _binding: FragmentVisitasListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitasListBinding.inflate(
            inflater,
            container,
            false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddVisita.setOnClickListener{
            view.findNavController().navigate(R.id.visitasCreateFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}