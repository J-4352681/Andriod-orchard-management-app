package app.lajusta.data

import android.util.Log
import app.lajusta.MainActivity.Companion.LOGIN_DEBUG
import app.lajusta.data.model.LoggedInUser
import app.lajusta.data.model.UserType
import app.lajusta.ui.login.api.LoginApi
import app.lajusta.ui.login.api.UsuarioLogin
import app.lajusta.ui.login.api.UsuarioLoginResponse
import app.lajusta.ui.usuarios.Usuario
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {


    fun login(username: String, password: String): Result<LoggedInUser> {
        try {

            if (LOGIN_DEBUG) {
                if (username == "administrador" && password == "administrador") {
                    val admin = LoggedInUser(-1, "administrador", 0, "TOKEN")
                    return Result.Success(admin)
                }

                if (username == "tecnico" && password == "tecnico") {
                    val tec = LoggedInUser(-2, "tecnico", 1, "token")
                    return Result.Success(tec)
                }

                if (username == "aaaaaa" && password == "aaaaaa") {
                    val tec = LoggedInUser(-3, "aaa", 4, "token")
                    return Result.Success(tec)
                }
            }


            return runBlocking {
                val user = UsuarioLogin(username, password)
                try {
                    val result = LoginApi().login(user)
                    if (result.isSuccessful) {
                        Log.i("token", result.body()?.token.orEmpty())

                        val tec = LoggedInUser(
                            result.body()!!.id_user,
                            username,
                            result.body()!!.rol,
                            result.body()!!.token
                        )
                        return@runBlocking Result.Success(tec)
                    } else {
                        Log.e("No fue exitoso ", "mal")
                        return@runBlocking Result.Error(Exception("Usuario no existente"))
                    }
                } catch (e: Exception) {
                    Log.e("RETROFIT_ERROR", e.message.orEmpty())
                    return@runBlocking Result.Error(Exception("ERROR AL BUSCAR DATOS"))
                }
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}