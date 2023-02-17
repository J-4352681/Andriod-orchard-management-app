package app.lajusta.ui.familia.api

import app.lajusta.MainActivity
import app.lajusta.ui.familia.Familia
import okhttp3.RequestBody
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
    suspend fun postFamilia(@Body familia: Familia): Response<Unit>

    @PUT("FamiliasProductoras")
    suspend fun putFamilia(@Body familia: Familia): Response<Unit>

    companion object {
        operator fun invoke() : FamiliaApi {
            return Retrofit.Builder()
                .baseUrl(MainActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FamiliaApi::class.java)
        }
    }
}