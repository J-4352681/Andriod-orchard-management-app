package app.lajusta.ui.bolson.api

import app.lajusta.MainActivity.Companion.baseUrl
import app.lajusta.ui.bolson.Bolson
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface BolsonApi {
    @GET("bolson")
    suspend fun getBolsones(): Response<List<Bolson>>

    @GET("bolson/")
    suspend fun getBolson(id: String): Response<Bolson>

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
