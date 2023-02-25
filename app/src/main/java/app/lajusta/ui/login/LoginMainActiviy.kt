package app.lajusta.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import app.lajusta.MainActivity
import app.lajusta.databinding.ActivityLoginMainBinding
import app.lajusta.ui.login.api.LoginApi
import app.lajusta.ui.login.api.UsuarioLogin
import app.lajusta.ui.usuarios.api.UsuariosApi
import kotlinx.coroutines.*

class LoginMainActiviy : AppCompatActivity() {
    private lateinit var binding: ActivityLoginMainBinding
    private val usuarioLogin = UsuarioLogin("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.username.afterTextChanged { usuarioLogin.username = it }
        binding.password.afterTextChanged { usuarioLogin.password = it }

        binding.login.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                var success = false

                val loginResponse = LoginApi().login(usuarioLogin)
                if (loginResponse.isSuccessful) {
                    val userResponse = UsuariosApi().getUsuario(loginResponse.body()!!.id_user)
                    if (userResponse.isSuccessful) runOnUiThread {
                        val bundle = bundleOf("usuario" to userResponse.body())
                        val mainActivity = Intent(
                            this@LoginMainActiviy, MainActivity::class.java
                        ).also { it.putExtras(bundle) }
                        success = true
                        startActivity(mainActivity)
                    }
                }

                if(!success) runOnUiThread { Toast.makeText(
                    this@LoginMainActiviy,
                    "Hubo un error. Intente luego nuevamente.",
                    Toast.LENGTH_SHORT
                ).show() }
            }
        }
    }
}