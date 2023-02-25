package app.lajusta.ui.usuarios.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import app.lajusta.R
import app.lajusta.databinding.FragmentUsuariosCreateBinding
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi

class UsuariosCreateFragment : BaseFragment() {
    private var _binding: FragmentUsuariosCreateBinding? = null
    private val binding get() = _binding!!
    //private var usuario = Usuario(0, "", "", "", "", "", "", 2)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsuariosCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillItem()
        setClickListeners()
    }

    private fun fillItem() {
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

        binding.bGuardar.setOnClickListener {

            val usuario = Usuario(
                0,
                binding.etNombre.text.toString().trim(),
                binding.etApellido.text.toString().trim(),
                binding.etDireccion.text.toString().trim(),
                binding.etUsername.text.toString().trim(),
                binding.etPassword.text.toString().trim(),
                binding.etEmail.text.toString().trim(),
                Usuario.rolNameToNumber(binding.sRol.selectedItem.toString())
            )

            if(usuario.nombre.isNullOrEmpty()) {
                shortToast("Debe seleccionar un nombre")
                return@setOnClickListener
            }
            if(usuario.apellido.isNullOrEmpty()) {
                shortToast("Debe seleccionar un apellido")
                return@setOnClickListener
            }
            if(usuario.direccion.isNullOrEmpty()) {
                shortToast("Debe seleccionar una direccion")
                return@setOnClickListener
            }
            if(usuario.username.isNullOrEmpty()) {
                shortToast("Debe seleccionar un nombre de usuario")
                return@setOnClickListener
            }
            if(usuario.password.isNullOrEmpty()) {
                shortToast("Debe seleccionar una contraseña")
                return@setOnClickListener
            }
            if(usuario.password != binding.etPasswordRepeat.text.toString().trim()) {
                shortToast("La contraseña repetida debe ser igual a la original")
                return@setOnClickListener
            }
            if(usuario.email.isNullOrEmpty()) {
                shortToast("Debe seleccionar un email ")
                return@setOnClickListener
            }
            if(usuario.roles != 1 && usuario.roles != 2) {
                shortToast("Debe seleccionar un rol ")
                return@setOnClickListener
            }

            returnSimpleApiCall({
                UsuariosApi().postUsuario(
                    usuario
                )
            }, "Hubo un error. El usuario no pudo ser creado.")

        }

        binding.bCancelar.setOnClickListener {
            activity?.onBackPressed()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

