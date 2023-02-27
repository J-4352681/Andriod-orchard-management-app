package app.lajusta.ui.visita.create

import app.lajusta.ui.visita.api.VisitaApi
import app.lajusta.ui.visita.edition.VisitaBaseEditionFragment

class VisitasCreateFragment : VisitaBaseEditionFragment() {
    override fun commitChange() =
        returnSimpleApiCall(
            { VisitaApi().postVisita(visita) },
            "Hubo un error. La visita no pudo ser creada."
        )

    override fun denyAction() {
        activity?.onBackPressed()
    }
}