package app.lajusta.ui.verdura.modify

import android.os.Bundle
import android.view.View
import app.lajusta.ui.verdura.api.VerduraApi
import app.lajusta.ui.verdura.edition.VerduraBaseEditionFragment

class VerduraModifyFragment: VerduraBaseEditionFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bDenyAction.text = "Borrar"
    }

    override fun denyAction() {
        returnSimpleApiCall(
            { VerduraApi().deleteVerdura(verdura.id_verdura) },
            "Hubo un error. La verdura no pudo ser eliminada."
        )
    }

    override fun commitChange() {
        returnSimpleApiCall(
            { VerduraApi().putVerdura(verdura) },
            "Hubo un error. La verdura no pudo ser creada."
        )
    }
}