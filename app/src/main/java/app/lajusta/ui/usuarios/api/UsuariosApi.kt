package app.lajusta.ui.usuarios.api

import app.lajusta.MainActivity.Companion.baseUrl
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.visita.Visita
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UsuariosApi {
    @GET("users")
    suspend fun getUsuarios(): Response<List<Usuario>>

    @GET("users/{id}")
    suspend fun getUsuario(@Path("id") id: Int): Response<Usuario>

    @DELETE("users/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Unit>

    @POST("users")
    suspend fun postUsuario(@Body usuario: Usuario): Response<Usuario>

    @PUT("users")
    suspend fun putUsuario(@Body usuario: Usuario): Response<Unit>

    companion object {
        operator fun invoke() : UsuariosApi {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UsuariosApi::class.java)
        }
    }
}
