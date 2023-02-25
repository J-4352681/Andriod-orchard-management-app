package app.lajusta.ui.ronda.modify

import android.os.Bundle
import android.view.View
import app.lajusta.ui.ronda.api.RondaApi
import app.lajusta.ui.ronda.edition.RondaBaseEditionFragment

class RondaModifyFragment: RondaBaseEditionFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bDenyAction.text = "Borrar"
    }

    override fun commitChange() =
        returnSimpleApiCall(
            { RondaApi().putRonda(ronda) },
            "Hubo un error. La ronda no pudo ser creada."
        )

    override fun denyAction() =
        returnSimpleApiCall(
            { RondaApi().deleteRonda(ronda.id_ronda) },
            "Hubo un error. La ronda no pudo ser eliminada."
        )
}