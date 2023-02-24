package app.lajusta.ui.bolson.create

import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.edition.BaseEditionFragment

class BolsonCreateFragment : BaseEditionFragment() {

    override fun commitChange() =
        returnSimpleApiCall(
            { BolsonApi().postBolson(bolson) },
            "Hubo un error. El bolsón no pudo ser creado."
        )

    override fun denyActionListener() {
        activity?.onBackPressed()
    }
}