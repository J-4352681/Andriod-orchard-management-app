package app.lajusta.ui.visita.modify

import android.os.Bundle
import android.view.View
import app.lajusta.ui.visita.api.VisitaApi
import app.lajusta.ui.visita.edition.VisitaBaseEditionFragment

class VisitaModifyFragment : VisitaBaseEditionFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bDenyAction.text = "Borrar"
    }

    override fun denyAction() {
        returnSimpleApiCall(
            { VisitaApi().deleteVisita(visita.id_visita) },
            "Hubo un error. No se pudo eliminar la visita."
        )
    }

    override fun commitChange() {
        returnSimpleApiCall(
            { VisitaApi().putVisita(visita) },
            "Hubo un error. La visita no pudo ser modificada."
        )
    }
}