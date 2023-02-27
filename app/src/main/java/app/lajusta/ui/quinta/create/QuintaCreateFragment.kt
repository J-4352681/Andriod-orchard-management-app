package app.lajusta.ui.quinta.create

import android.os.Bundle
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.PrefilledQuinta
import app.lajusta.ui.quinta.edition.QuintaBaseEditionFragment

class QuintaCreateFragment: QuintaBaseEditionFragment() {
    override fun denyAction() {
        activity?.onBackPressed()
    }

    override fun commitChange() {
        returnSimpleApiCall(
            { QuintaApi().postQuinta(quinta) },
            "Hubo un error. La quinta no pudo ser creada."
        )
    }
}