package app.lajusta.ui.visita.api

import app.lajusta.api.SafeApiRequest

class VisitaProvider(
    private val api: VisitaApi
): SafeApiRequest() {
    suspend fun getVisitas() = apiRequest { api.getVisitas() }
}