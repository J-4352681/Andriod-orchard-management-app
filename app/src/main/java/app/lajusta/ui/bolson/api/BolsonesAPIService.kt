package app.lajusta.ui.bolson.api

import app.lajusta.R
import app.lajusta.ui.bolson.Bolson
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface BolsonesAPIService {
    @GET("bolson")
    suspend fun getBolsones(): Response<List<Bolson>>

    companion object {
        operator fun invoke(): BolsonesAPIService {
            val url: String = R.string.url_base.toString()
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://official-joke-api.appspot.com/jokes/ten/")  // API de prueba para testear lista
                .build()
                .create(BolsonesAPIService::class.java)
        }
    }
}
