package app.lajusta.data

import android.util.Log
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
            // TODO: handle loggedInUser authentication

            if (username == "administrador" && password == "administrador") {
                val admin = LoggedInUser(38, "administrador", UserType.ADMIN, "TOKEN")
                return Result.Success(admin)
            }

            if (username == "tecnico" && password == "tecnico") {
                val tec = LoggedInUser(44, "tecnico", UserType.TECNICO, "token")
                return Result.Success(tec)
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
                            Usuario.rolNumberToUserType(result.body()!!.rol)!!,
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

    private fun intToUserType(num: Int?): UserType? {
        if (num == 1) return UserType.ADMIN
        else if (num == 2) return UserType.TECNICO
        return null
    }
}