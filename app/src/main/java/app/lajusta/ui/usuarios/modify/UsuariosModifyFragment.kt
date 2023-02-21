package app.lajusta.ui.usuarios.modify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import app.lajusta.R
import app.lajusta.databinding.FragmentRondaModifyBinding
import app.lajusta.databinding.FragmentUsuariosModifyBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi

class UsuariosModifyFragment : BaseFragment() {
    private var _binding: FragmentUsuariosModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            usuario = bundle.getParcelable("usuario")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsuariosModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()
    }

    private fun fillItem() {
        binding.etNombre.setText(usuario.nombre)
        binding.etApellido.setText(usuario.apellido)
        binding.etDireccion.setText(usuario.direccion)
        binding.etEmail.setText(usuario.email)

        val nombresRoles: Array<String> = Usuario.getRolNames()

        val spinnnerRoles = binding.sRol
        val adapterRoles: ArrayAdapter<String>? = (activity?.let {
            ArrayAdapter<String>(
                it,
                R.layout.spinner_item, nombresRoles
            )
        })

        spinnnerRoles.adapter = adapterRoles
    }

    private fun setClickListeners() {
        binding.bBorrar.setOnClickListener {
            returnSimpleApiCall(
                { UsuariosApi().deleteUsuario(usuario.id_user) },
                "Hubo un error. El usuario no pudo ser eliminado."
            )
        }

        binding.bGuardar.setOnClickListener {

            val modifiedUsuario = Usuario(
                usuario.id_user,
                binding.etNombre.text.toString().trim(),
                binding.etApellido.text.toString().trim(),
                binding.etDireccion.text.toString().trim(),
                usuario.username,
                usuario.password,
                binding.etEmail.text.toString().trim(),
                Usuario.rolNameToNumber(binding.sRol.selectedItem.toString())
            )

            if(modifiedUsuario.nombre.isNullOrEmpty()) {
                shortToast("Debe seleccionar un nombre")
                return@setOnClickListener
            }
            if(modifiedUsuario.apellido.isNullOrEmpty()) {
                shortToast("Debe seleccionar un apellido")
                return@setOnClickListener
            }
            if(modifiedUsuario.direccion.isNullOrEmpty()) {
                shortToast("Debe seleccionar una direccion")
                return@setOnClickListener
            }
            if(modifiedUsuario.username.isNullOrEmpty()) {
                shortToast("Debe seleccionar un nombre de usuario")
                return@setOnClickListener
            }
            if(modifiedUsuario.password.isNullOrEmpty()) {
                shortToast("Debe seleccionar una contraseña")
                return@setOnClickListener
            }
            if(modifiedUsuario.email.isNullOrEmpty()) {
                shortToast("Debe seleccionar un email ")
                return@setOnClickListener
            }
            if(modifiedUsuario.roles != 1 && modifiedUsuario.roles != 2) {
                shortToast("Debe seleccionar un rol ")
                return@setOnClickListener
            }


            returnSimpleApiCall(
                { UsuariosApi().putUsuario(modifiedUsuario) },
                "Hubo un error. El usuario no pudo ser modificado."
            )
        }
    }

}