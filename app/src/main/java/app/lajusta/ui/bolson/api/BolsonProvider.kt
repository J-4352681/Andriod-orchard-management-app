package app.lajusta.ui.bolson.api

import app.lajusta.api.SafeApiRequest

class BolsonProvider(
    private val api: BolsonApi
): SafeApiRequest() {
    suspend fun getBolsones() = apiRequest { api.getBolsones() }
}