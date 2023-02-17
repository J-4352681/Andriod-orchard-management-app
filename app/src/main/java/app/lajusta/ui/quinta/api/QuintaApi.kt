package app.lajusta.ui.quinta.api

import app.lajusta.MainActivity.Companion.baseUrl
import app.lajusta.ui.quinta.Quinta
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface QuintaApi {
    @GET("Quintas")
    suspend fun getQuintas(): Response<List<Quinta>>

    @GET("Quintas/{id}")
    suspend fun getQuinta(@Path("id") id: Int): Response<Quinta>

    @DELETE("Quintas/{id}")
    suspend fun deleteQuinta(@Path("id") id: Int): Response<Unit>

    @POST("Quintas")
    suspend fun postQuinta(@Body requestBody: RequestBody): Response<RequestBody>

    @PUT("Quintas/{id}")
    suspend fun putQuinta(@Path("id") id: Int, @Body quinta: Quinta): Response<Unit>

    companion object {
        operator fun invoke() : QuintaApi {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuintaApi::class.java)
        }
    }
}
