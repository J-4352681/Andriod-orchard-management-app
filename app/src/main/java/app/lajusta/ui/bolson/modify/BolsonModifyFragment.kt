package app.lajusta.ui.bolson.modify

import android.os.Bundle
import android.view.View
import app.lajusta.ui.bolson.api.BolsonApi
import app.lajusta.ui.bolson.edition.BolsonBaseEditionFragment

class BolsonModifyFragment: BolsonBaseEditionFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bDenyAction.text = "Borrar"
    }

    override fun commitChange() =
        returnSimpleApiCall(
            { BolsonApi().putBolson(bolson) },
            "Hubo un error. El bolsón no pudo ser creado."
        )

    override fun denyAction() {
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