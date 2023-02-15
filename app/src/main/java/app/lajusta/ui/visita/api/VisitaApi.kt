package app.lajusta.ui.visita.api

import app.lajusta.MainActivity.Companion.baseUrl
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.visita.Visita
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface VisitaApi {
    @GET("Visitas")
    suspend fun getVisitas(): Response<List<Visita>>

    @GET("Visitas/{id}")
    suspend fun getVisita(@Path("id") id: Int): Response<Visita>

    @DELETE("Visitas/{id}")
    suspend fun deleteVisita(@Path("id") id: Int): Response<Unit>

    @POST("Visitas")
    suspend fun postVisita(@Body requestBody: RequestBody): Response<RequestBody>

    @PUT("Visitas")
    suspend fun putVisita(@Body visita: Visita): Response<Unit>

    companion object {
        operator fun invoke() : VisitaApi {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VisitaApi::class.java)
        }
    }
}
