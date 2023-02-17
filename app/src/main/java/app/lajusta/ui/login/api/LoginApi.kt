package app.lajusta.ui.login.api

import app.lajusta.MainActivity.Companion.baseUrl
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LoginApi {
    @POST("auth")
    suspend fun login(@Body user: UsuarioLogin): Response<UsuarioLoginResponse>


    companion object {
        operator fun invoke() : LoginApi {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginApi::class.java)
        }
    }
}
