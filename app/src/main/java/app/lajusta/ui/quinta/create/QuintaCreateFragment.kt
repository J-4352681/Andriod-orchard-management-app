package app.lajusta.ui.quinta.create

import android.os.Bundle
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.PrefilledQuinta
import app.lajusta.ui.quinta.edition.QuintaBaseEditionFragment

class QuintaCreateFragment: QuintaBaseEditionFragment() {
    private var prefilledQuinta: PrefilledQuinta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            if (bundle.containsKey("prefilledQuinta"))
                prefilledQuinta = bundle.getParcelable("prefilledQuinta")
        }
    }

    override fun denyAction() {
        activity?.onBackPressed()
    }

    override fun commitChange() {
        returnSimpleApiCall(
            { QuintaApi().postQuinta(quinta) },
            "Hubo un error. La quinta no pudo ser creada."
        )
    }

    override fun withApiData() = prefillQuinta()

    private fun prefillQuinta() {
        if(prefilledQuinta != null) {
            if(prefilledQuinta!!.geoImg != null) {
                binding.etImagen.setText(prefilledQuinta!!.geoImg)
                if (prefilledQuinta!!._block) binding.etImagen.isEnabled = false
            }
            if(prefilledQuinta!!.nombre != null) {
                binding.etNombre.setText(prefilledQuinta!!.nombre)
                if (prefilledQuinta!!._block) binding.etNombre.isEnabled = false
            }
            if(prefilledQuinta!!.direccion != null) {
                binding.etDireccion.setText(prefilledQuinta!!.direccion)
                if (prefilledQuinta!!._block) binding.etDireccion.isEnabled = false
            }
            if(prefilledQuinta!!.fpId != null) {
                binding.sFamilia.setSelection(
                    familiasAdapter.getPosition(
                        familias.find { it.id_fp == prefilledQuinta!!.fpId!! }
                    )
                )
                if (prefilledQuinta!!._block) binding.sFamilia.isEnabled = false
            }
        }
    }
}