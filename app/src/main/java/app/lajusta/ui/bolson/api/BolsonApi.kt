package app.lajusta.ui.bolson.api

import app.lajusta.MainActivity.Companion.baseUrl
import app.lajusta.ui.bolson.Bolson
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface BolsonApi {
    @GET("bolson")
    suspend fun getBolsones(): Response<List<Bolson>>

    @GET("bolson/")
    suspend fun getBolson(id: String): Response<Bolson>

    @DELETE("bolson/{id}")
    suspend fun deleteBolson(@Path("id") id: Int): Response<Unit>

    @POST("bolson")
    suspend fun postBolson(@Body requestBody: RequestBody): Response<RequestBody>

companion object {
        operator fun invoke() : BolsonApi {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BolsonApi::class.java)
        }
    }
}
