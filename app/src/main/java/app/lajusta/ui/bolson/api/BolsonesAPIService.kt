package app.lajusta.ui.bolson.api

import app.lajusta.R
import app.lajusta.ui.bolson.BolsonDataclass
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface BolsonesAPIService {
    @GET("bolson")
    suspend fun getBolsones(): Response<List<BolsonDataclass>>

    companion object {
        operator fun invoke(): BolsonesAPIService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(R.string.url_base.toString())
                .build()
                .create(BolsonesAPIService::class.java)
        }
    }
}
