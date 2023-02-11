package app.lajusta.ui.bolson.api

import app.lajusta.ui.api.SafeAPIRequest

class BolsonesReposiory(
    private val api: BolsonesAPIService
): SafeAPIRequest() {
    suspend fun getBolsones() = apiRequest { api.getBolsones() }
}