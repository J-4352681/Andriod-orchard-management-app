package app.lajusta.ui.rondas.api

import app.lajusta.MainActivity.Companion.baseUrl
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.rondas.Ronda
import okhttp3.RequestBody
import retrofit2.Call
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
    suspend fun postRonda(@Body requestBody: RequestBody): Response<RequestBody>

    @PUT("rondas/{id}")
    suspend fun putRonda(@Path("id") id: Int, @Body ronda: Ronda): Response<Unit>

    companion object {
        operator fun invoke() : RondaApi {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RondaApi::class.java)
        }
    }
}
