package app.lajusta.ui.bolson.api

import app.lajusta.api.SafeApiRequest
import app.lajusta.ui.bolson.api.BolsonesApi

class BolsonesProvider(
    private val api: BolsonesApi
): SafeApiRequest() {
    suspend fun getBolsones() = apiRequest { api.getBolsones() }
}