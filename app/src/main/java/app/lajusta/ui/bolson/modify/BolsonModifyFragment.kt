package app.lajusta.ui.bolson.modify

import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.edition.BaseEditionFragment

class BolsonModifyFragment: BaseEditionFragment() {

    override fun commitChange() =
        returnSimpleApiCall(
            { BolsonApi().putBolson(bolson) },
            "Hubo un error. El bolsón no pudo ser creado."
        )

    override fun denyActionListener() {
        if(binding.etCantidad.text.toString().isEmpty() || bolson.cantidad == 0) {
            shortToast("Debe escribir una cantidad")
            return
        }

        returnSimpleApiCall(
            { BolsonApi().deleteBolson(bolson.id_bolson) },
            "Hubo un error. El bolsón no pudo ser eliminado."
        )
    }
}