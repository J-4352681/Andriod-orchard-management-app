package app.lajusta.data

import app.lajusta.data.model.LoggedInUser
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

            if (username == "administrador" && password == "administrador") {
                val admin = LoggedInUser(java.util.UUID.randomUUID().toString(), "Admin")
                return Result.Success(admin)
            }

            if (username == "tecnico" && password == "tecnico") {
                val tec = LoggedInUser(java.util.UUID.randomUUID().toString(), "Tecnico")
                return Result.Success(tec)
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
}