package app.lajusta.ui.ronda.create

import app.lajusta.ui.ronda.api.RondaApi
import app.lajusta.ui.ronda.edition.RondaBaseEditionFragment

class RondaCreateFragment: RondaBaseEditionFragment() {
    override fun commitChange() =
        returnSimpleApiCall(
            { RondaApi().postRonda(ronda) },
            "Hubo un error. La ronda no pudo ser creada."
        )

    override fun denyAction() = activity!!.onBackPressed()
}