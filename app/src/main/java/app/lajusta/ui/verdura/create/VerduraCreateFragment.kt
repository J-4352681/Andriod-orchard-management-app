package app.lajusta.ui.verdura.create

import app.lajusta.ui.verdura.api.VerduraApi
import app.lajusta.ui.verdura.edition.VerduraBaseEditionFragment

class VerduraCreateFragment: VerduraBaseEditionFragment() {
    override fun commitChange() {
        returnSimpleApiCall(
            { VerduraApi().postVerdura(verdura) },
            "Hubo un error. La verdura no pudo ser creada."
        )
    }

    override fun denyAction() {
        activity!!.onBackPressed()
    }
}