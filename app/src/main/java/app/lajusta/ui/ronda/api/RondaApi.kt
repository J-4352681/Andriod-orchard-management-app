package app.lajusta.ui.ronda.api

import app.lajusta.MainActivity
import app.lajusta.ui.ronda.Ronda
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RondaApi {
    @GET("rondas")
    suspend fun getRondas(): Response<List<Ronda>>

    @GET("rondas/{id}")
    suspend fun getRonda(@Path("id") id: Int): Response<Ronda>

    @DELETE("rondas/{id}")
    suspend fun deleteRonda(@Path("id") id: Int): Response<Unit>

    @POST("rondas")
    suspend fun postRonda(@Body ronda: Ronda): Response<Unit>

    @PUT("rondas")
    suspend fun putRonda(@Body ronda: Ronda): Response<Unit>

    companion object {
        operator fun invoke() : RondaApi {
            return Retrofit.Builder()
                .baseUrl(MainActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RondaApi::class.java)
        }
    }
}
