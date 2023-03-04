package app.lajusta

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import app.lajusta.data.Preferences.PreferenceHelper.loggedIn
import app.lajusta.data.Preferences.PreferenceHelper.userId
import app.lajusta.data.Preferences.PreferenceHelper.username
import app.lajusta.data.Result
import app.lajusta.data.model.LoggedInUser
import app.lajusta.ui.login.LoginActivity
import app.lajusta.ui.login.api.LoginApi
import app.lajusta.ui.login.api.UsuarioLogin
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.usuarios.api.UsuariosApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

class ProfileActivity : AppCompatActivity() {

    val CUSTOM_PREF_NAME = "User_data"
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        prefs = applicationContext.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)

        //CHECKING LOGIN
        if (!prefs.loggedIn) goToLogin()

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Perfil"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Username
        val editText = findViewById<EditText>(R.id.et_ch_username)
        editText.hint = prefs.username

        val btnUsername = findViewById<Button>(R.id.btn_ch_userneme)
        btnUsername.setOnClickListener {
            val newUsername = editText.text.toString()
            if (editText.text.toString().isNotEmpty()) {
                showAlertUsername(newUsername)
            } else toast("Primero debe escribir un nuevo nombre de usuario.")
        }

        val oldPassword = findViewById<EditText>(R.id.et_ch_password_old)
        val newPassword = findViewById<EditText>(R.id.et_ch_password_new)
        val newPasswordRepeat = findViewById<EditText>(R.id.et_ch_password_new_repeat)
        val btnPasswordChange = findViewById<Button>(R.id.btn_ch_password)
        btnPasswordChange.setOnClickListener {
            val oldPassText = oldPassword.text.toString()
            val newPassText = newPassword.text.toString()
            val newPassRepText = newPasswordRepeat.text.toString()
            if (oldPassText.isEmpty()) toast("Ingresar contraseña antigua")

            if (newPassText.isEmpty()) toast("Ingresar contraseña nueva")

            if (newPassRepText.isEmpty()) toast("Repetir contraseña nueva")

            if (newPassText != newPassRepText) toast("Repetir contraseña nueva correctamente")

            showAlertPasswordChange(oldPassText, newPassText)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showAlertUsername(newUsername: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Esto cambiara su nombre de usuario a $newUsername")
        builder.setMessage("¿Quiere continuar?")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
        builder.setPositiveButton("Si") { dialog, which ->
            changeUsername(newUsername)
        }
        builder.setNegativeButton("Cancelar", null)

        builder.show()

    }

    private fun changeUsername(newUsername: String) {

        if (prefs.userId == null || newUsername.isEmpty()) {
            toastLong("Ningun usuario logeado")
            goToLogin()
            return
        }

        runBlocking {
            var usuario: Usuario
            try {
                val result = UsuariosApi().getUsuario(prefs.userId)
                if (result.isSuccessful) {
                    usuario = result.body()!!


                    val modifiedUsuario = Usuario(
                        usuario.id_user,
                        usuario.nombre,
                        usuario.apellido,
                        usuario.direccion,
                        newUsername,
                        usuario.password,
                        usuario.email,
                        usuario.roles
                    )

                    returnSimpleApiCallProgressBar(
                        { UsuariosApi().putUsuario(modifiedUsuario) },
                        "Cambio de nombre de usuario fallo, intente mas tarde.",
                        {
                            prefs.username = newUsername
                            toastLong("Nombre de usuario cambiado a ")
                        },
                        findViewById(R.id.profileProgressBar)
                    )
                } else {
                    toastLong("Usuario no existe.")
                }

            } catch (e: Exception) {
                Log.e("RETROFIT ERROR", e.message!!)
                toastLong("Hubo un error.")
            }
        }

    }

    private fun showAlertPasswordChange(oldPassword: String, newPassword: String) {

        if (!prefs.loggedIn) {
            toastLong("Ningun usuario logeado")
            goToLogin()
            return
        }

        if ( newPassword.isEmpty() || oldPassword.isEmpty()) {
            toastLong("Complete los campos de contraseña con la actual y la nueva.")
            return
        }

        if ( Usuario.checkPassword(newPassword) ) {
            toastLong("La nueva contraseña debe teenr al menos 5 caracteres.")
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Esto cambiara su contraseña")
        builder.setMessage("¿Quiere continuar?")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
        builder.setPositiveButton("Si") { dialog, which ->
            //Toast.makeText(applicationContext,
            //    "Si", Toast.LENGTH_SHORT).show()

            changePassword(oldPassword, newPassword)
        }
        builder.setNegativeButton("Cancelar", null)

        builder.show()

    }

    private fun changePassword(oldPassword: String, newPassword: String) {

        runBlocking {
            val usuario: Usuario
            try {
                val result = UsuariosApi().getUsuario(prefs.userId)
                if (result.isSuccessful) {
                    usuario = result.body()!!
                    if (usuario.password != oldPassword) {
                        Toast.makeText(
                            applicationContext,
                            "Contraseña antigua es incorrecta", Toast.LENGTH_LONG
                        ).show()
                    } else {

                        val modifiedUsuario = Usuario(
                            usuario.id_user,
                            usuario.nombre,
                            usuario.apellido,
                            usuario.direccion,
                            usuario.username,
                            newPassword,
                            usuario.email,
                            usuario.roles
                        )

                        returnSimpleApiCallProgressBar(
                            { UsuariosApi().putUsuario(modifiedUsuario) },
                            "Cambio de contraseña fallo, intente mas tarde.",
                            { toastLong("CAmbio de contraseña exitoso.")},
                            findViewById(R.id.profileProgressBar)
                        )
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Usuario no existe.", Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("RETROFIT ERROR", e.message!!)
                Toast.makeText(
                    applicationContext,
                    "Hubo un error.", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun returnSimpleApiCall(
        apiEffectiveCall: suspend () -> Response<out Any>,
        failureMessage: String
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = apiEffectiveCall()
                if (!response.isSuccessful) throw Exception(response.code().toString())
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    failureMessage, Toast.LENGTH_LONG
                ).show()
            } finally {
                finish()
            }
        }
    }

    private fun returnSimpleApiCallProgressBar(
        apiEffectiveCall: suspend () -> Response<out Any>,
        failureMessage: String,
        apiCallUIBlock: () -> Unit,
        progressBar: ProgressBar
    ) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = apiEffectiveCall()
                if (!response.isSuccessful) throw Exception(response.code().toString())
                else apiCallUIBlock()
            } catch (e: Exception) {
                toast(failureMessage)

            } finally {
                progressBar.visibility = View.GONE
                onBackPressed()
            }
        }
    }

    fun toast(message: String) = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
    fun toastLong(message: String) =
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)

    private fun goToLogin() {
        val i = Intent(applicationContext, LoginActivity::class.java)
        startActivity(i)
        finish()
    }
}