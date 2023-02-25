package app.lajusta

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import app.lajusta.data.Result
import app.lajusta.data.model.LoggedInUser
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //CHECKING LOGIN
        /*if ( MainActivity.userName.isEmpty() || MainActivity.userType.isEmpty() || MainActivity.userId.isEmpty() ) {
            goToLogin()
        }*/

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Perfil"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Username
        val editText = findViewById<EditText>(R.id.et_ch_username)
        editText.hint = MainActivity.userName

        val btnUsername = findViewById<Button>(R.id.btn_ch_userneme)
        btnUsername.setOnClickListener {
            val newUsername = editText.text.toString().orEmpty()
            if (editText.text.toString().isNotEmpty()) {
                showAlertUsername(newUsername)
            } else {
                Toast.makeText(
                    this@ProfileActivity,
                    "Primero debe escribir un nuevo nombre de usuario.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        val oldPassword = findViewById<EditText>(R.id.et_ch_password_old)
        val newPassword = findViewById<EditText>(R.id.et_ch_password_new)
        val newPasswordRepeat = findViewById<EditText>(R.id.et_ch_password_new_repeat)
        val btnPasswordChange = findViewById<Button>(R.id.btn_ch_password)
        btnPasswordChange.setOnClickListener {
            val oldPassText = oldPassword.text.toString()
            val newPassText = newPassword.text.toString()
            val newPassRepText = newPasswordRepeat.text.toString()
            if (oldPassText.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Ingresar contraseña antigua",
                    Toast.LENGTH_SHORT
                ).show()
            } else
                if (newPassText.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Ingresar contraseña nueva",
                        Toast.LENGTH_SHORT
                    ).show()
                } else
                    if (newPassRepText.isEmpty()) {
                        Toast.makeText(
                            applicationContext,
                            "Repetir contraseña nueva",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else
                        if (newPassText != newPassRepText) {
                            Toast.makeText(
                                applicationContext,
                                "Repetir contraseña nueva correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            showAlertPasswordChange(oldPassText, newPassText)
                        }

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
            //Toast.makeText(applicationContext,
            //    "Si", Toast.LENGTH_SHORT).show()

            changeUsername(newUsername)
        }
        builder.setNegativeButton("Cancelar", null)

        builder.show()

    }

    private fun changeUsername(newUsername: String) {

        if (MainActivity.userId == null || newUsername.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Ningun usuario logeado", Toast.LENGTH_LONG
            ).show()
            return
        }

        runBlocking {
            var usuario: Usuario
            try {
                val result = UsuariosApi().getUsuario(MainActivity.userId!!)
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

                    returnSimpleApiCall(
                        { UsuariosApi().putUsuario(modifiedUsuario) },
                        "Cambio de nombre de usuario fallo, intente mas tarde."
                    )
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

    private fun showAlertPasswordChange(oldPassword: String, newPassword: String) {
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

        if (MainActivity.userId == null || newPassword.isEmpty() || oldPassword.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Ningun usuario logeado", Toast.LENGTH_LONG
            ).show()
            //goToLogin()
            return
        }



        runBlocking {
            var usuario: Usuario
            try {
                val result = UsuariosApi().getUsuario(MainActivity.userId!!)
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

                        returnSimpleApiCall(
                            { UsuariosApi().putUsuario(modifiedUsuario) },
                            "Cambio de contraseña fallo, intente mas tarde."
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

    fun returnSimpleApiCall(
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

}