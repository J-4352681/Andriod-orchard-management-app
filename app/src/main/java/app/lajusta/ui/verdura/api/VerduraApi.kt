package app.lajusta.ui.verdura.api

import app.lajusta.MainActivity
import app.lajusta.ui.verdura.Verdura
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface VerduraApi {
    @GET("verdura")
    suspend fun getVerduras(): Response<List<Verdura>>

    @GET("verdura/{id}")
    suspend fun getVerdura(@Path("id") id: Int): Response<Verdura>

    @DELETE("verdura/{id}")
    suspend fun deleteVerdura(@Path("id") id: Int): Response<Unit>


    @POST("verdura")
    suspend fun postVerdura(@Body verdura: Verdura): Response<Verdura>

    @PUT("verdura")
    suspend fun putVerdura(@Body verdura: Verdura): Response<Unit>

    companion object {
        operator fun invoke() : VerduraApi {
            return Retrofit.Builder()
                .baseUrl(MainActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VerduraApi::class.java)
        }
    }
}