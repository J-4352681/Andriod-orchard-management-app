package app.lajusta.data

import android.util.Log
import app.lajusta.data.model.LoggedInUser
import app.lajusta.data.model.UserType
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.login.api.LoginApi
import app.lajusta.ui.login.api.UsuarioLogin
import app.lajusta.ui.login.api.UsuarioLoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
            // Agregar llamada a una API de usuarios para verificar si el usuario nuevo forma parte de ellos
            // Por ahora voy a hacer 2 usuarios, uno admin y otro tecnico
            //java.util.UUID.randomUUID().toString()

            if (username == "administrador" && password == "administrador") {
                val admin = LoggedInUser(38, "administrador", UserType.ADMIN, "TOKEN")
                return Result.Success(admin)
            } else

            if (username == "tecnico" && password == "tecnico") {
                val tec = LoggedInUser(44, "tecnico", UserType.TECNICO, "token")
                return Result.Success(tec)
            }

            Log.e("Aviso", " llego al login data resourse")

            var data:Response<UsuarioLoginResponse>? = null

            CoroutineScope(Dispatchers.IO).launch { /** NO ESTA FUNCIONANDO, NO SE COMPLETA ANTES DE LO SIGUIENTE*/

                    val user = UsuarioLogin(username, password )
                    try {
                        val result = LoginApi().login(user)
                        data = result
                    } catch(e: Exception) {
                        Log.e("RETROFIT_ERROR", e.message.orEmpty())
                    }

            }

            val test = data
            test?.let {
                if(it.isSuccessful) {
                    println("Codigo ${it.code()}")
                    println(it.body()?.token)
                    val tec = LoggedInUser(it.body()?.id_user,username , intToUserType(it.body()?.rol), it.body()?.token)
                    return Result.Success(tec)
                } else{
                    println("Codigo ${it.code()}")
                    println(it.errorBody())
                    Log.e("RETROFIT_ERROR ", "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
                }
            }







            //val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            //return Result.Success(fakeUser)
            return Result.Error(Exception("Usuario no existente (administrador administrador)"));
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

    private fun intToUserType(num:Int?):UserType?{
        if (num == 1) return UserType.ADMIN
        else if (num == 2) return UserType.TECNICO
        return null
    }
}