package app.lajusta.ui.familia.api

import app.lajusta.MainActivity.Companion.baseUrl
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.rondas.Ronda
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FamiliaApi {
    @GET("FamiliasProductoras")
    suspend fun getFamilias(): Response<List<Familia>>

    @GET("FamiliasProductoras/{id}")
    suspend fun getFamilia(@Path("id") id: Int): Response<Familia>

    @DELETE("FamiliasProductoras/{id}")
    suspend fun deleteFamilia(@Path("id") id: Int): Response<Unit>

    @POST("FamiliasProductoras")
    suspend fun postFamilia(@Body requestBody: RequestBody): Response<RequestBody>

    @PUT("FamiliasProductoras/{id}")
    suspend fun putFamilia(@Path("id") id: Int, @Body familia: Familia): Response<Unit>

    companion object {
        operator fun invoke() : FamiliaApi {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FamiliaApi::class.java)
        }
    }
}
