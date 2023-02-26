package app.lajusta.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.lajusta.MainActivity
import app.lajusta.data.Preferences.PreferenceHelper.loggedIn
import app.lajusta.data.Preferences.PreferenceHelper.token
import app.lajusta.data.Preferences.PreferenceHelper.userId
import app.lajusta.data.Preferences.PreferenceHelper.userType
import app.lajusta.data.Preferences.PreferenceHelper.username
import app.lajusta.databinding.ActivityLoginMainBinding
import app.lajusta.ui.login.api.LoginApi
import app.lajusta.ui.login.api.UsuarioLogin
import app.lajusta.ui.login.api.UsuarioLoginResponse
import kotlinx.coroutines.*

class LoginMainActiviy : AppCompatActivity() {
    private lateinit var binding: ActivityLoginMainBinding
    private val usuarioLogin = UsuarioLogin("", "")

    private val CUSTOM_PREF_NAME = "User_data"
    private val prefs =
        applicationContext.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.username.afterTextChanged { usuarioLogin.username = it }
        binding.password.afterTextChanged { usuarioLogin.password = it }

        binding.login.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val loginResponse = LoginApi().login(usuarioLogin)
                if (loginResponse.isSuccessful) {
                    setSharedPreferences(loginResponse.body()!!)
                    val mainActivity =
                        Intent(this@LoginMainActiviy, MainActivity::class.java)
                    startActivity(mainActivity)
                } else runOnUiThread { Toast.makeText(
                    this@LoginMainActiviy,
                    "Hubo un error. Intente luego nuevamente.",
                    Toast.LENGTH_SHORT
                ).show() }
            }
        }
    }

    private fun setSharedPreferences(ulr: UsuarioLoginResponse) {
        prefs.userId = ulr.id_user
        prefs.username = usuarioLogin.username
        prefs.userType = ulr.rol
        prefs.token = ulr.token
        prefs.loggedIn = true
    }
}