package app.lajusta.ui.parcela.api

import app.lajusta.MainActivity
import app.lajusta.ui.parcela.Parcela
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ParcelaApi {
    @GET("Parcelas")
    suspend fun getParcelas(): Response<List<Parcela>>

    @GET("Parcelas/{id}")
    suspend fun getParcela(@Path("id") id: Int): Response<Parcela>

    @DELETE("Parcelas/{id}")
    suspend fun deleteParcela(@Path("id") id: Int): Response<Unit>


    @POST("Parcelas")
    suspend fun postParcela(@Body parcela: Parcela): Response<Parcela>

    @PUT("Parcelas")
    suspend fun putParcela(@Body parcela: Parcela): Response<Unit>

    companion object {
        operator fun invoke() : ParcelaApi {
            return Retrofit.Builder()
                .baseUrl(MainActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ParcelaApi::class.java)
        }
    }
}