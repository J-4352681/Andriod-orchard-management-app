package app.lajusta.ui.bolson.api

import app.lajusta.ui.bolson.Bolson
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface BolsonesAPIService {
    @GET
    suspend fun getBolsones(@Url url: String): Response<List<Bolson>>
}
