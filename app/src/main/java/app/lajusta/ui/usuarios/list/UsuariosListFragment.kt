package app.lajusta.ui.usuarios.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.lajusta.R
import app.lajusta.databinding.FragmentUsuariosListBinding
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi

class UsuariosListFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentUsuariosListBinding? = null
    private val binding get() = _binding!!
    private var usuariosApi = listOf<Usuario>()
    private val usuarios = mutableListOf<Usuario>()
    private val usuariosOriginal = mutableListOf<Usuario>()
    private var usuariosArg: MutableList<Usuario>? = null
    private lateinit var usuariosAdapter: UsuariosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            val data = bundle.getParcelableArrayList<Usuario>("usuarios")
            if(data != null) usuariosArg = data.toMutableList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuariosListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrearUsuario.setOnClickListener {
            this.findNavController().navigate(R.id.usuariosCreateFragment)
        }

        binding.svUsuarios.setOnQueryTextListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        usuariosAdapter = UsuariosAdapter(usuarios) { usuario: Usuario ->
            val bundle = bundleOf("usuario" to usuario)
            this.findNavController().navigate(R.id.usuariosModifyFragment, bundle)
        }
        binding.rvUsuarios.layoutManager = LinearLayoutManager(activity)
        binding.rvUsuarios.adapter = usuariosAdapter
        listInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listInit() {
        apiCallProgressBar(suspend {
            usuariosApi = UsuariosApi().getUsuarios().body()!!
        }, {
            usuarios.clear()
            if(usuariosArg != null) usuarios.addAll(usuariosArg!!)
            else usuarios.addAll(usuariosApi)

            usuariosOriginal.clear()
            usuariosOriginal.addAll(usuarios)

            actualizarListaUI("No se encontraron usuarios en el sistema.")
        }, "Hubo un error al actualizar la lista de usuarios.", binding.progressBar )
    }

    private fun filter(query: String?) {
        Usuario.filter(usuarios, usuariosOriginal, query)
        actualizarListaUI("No se encontraron usuarios que coincidan en el sistema.")
    }

    private fun actualizarListaUI ( emptyMessage:String ) {
        usuarios.sort()
        usuariosAdapter.notifyDataSetChanged()
        if (usuariosAdapter.itemCount == 0) shortToast(emptyMessage)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(query: String?): Boolean {
        filter(query!!.lowercase())
        return true
    }
}