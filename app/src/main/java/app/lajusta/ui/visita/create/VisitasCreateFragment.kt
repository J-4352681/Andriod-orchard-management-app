package app.lajusta.ui.visita.create

import android.os.Bundle
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.visita.PrefilledVisita
import app.lajusta.ui.visita.api.VisitaApi
import app.lajusta.ui.visita.edition.VisitaBaseEditionFragment

class VisitasCreateFragment : VisitaBaseEditionFragment() {
    private var prefilledVisita: PrefilledVisita? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            if (bundle.containsKey("prefilledVisita"))
                prefilledVisita = bundle.getParcelable("prefilledVisita")!!
        }
    }

    override fun withApiData() = prefillFields()

    private fun prefillFields() {
        if(prefilledVisita != null) {
            if (!prefilledVisita!!.descripcion.isNullOrEmpty()) {
                binding.etDesc.setText(prefilledVisita?.descripcion)
                if (prefilledVisita!!._blocked) binding.etDesc.isEnabled = false
            }
            if (!prefilledVisita!!.fecha_visita.isNullOrEmpty()) {
                visita.fecha_visita = prefilledVisita?.fecha_visita!!
                binding.tvFechaSeleccionada.text = ArrayedDate.toString(visita.fecha_visita)
                if (prefilledVisita!!._blocked) binding.bFecha.isEnabled = false
            }
            if (prefilledVisita!!.id_quinta != null) {
                visita.id_quinta = prefilledVisita?.id_quinta!!
                binding.spinnerQuinta.setSelection(
                    quintasAdapter.getPosition(quintas.find {
                        it.id_quinta == prefilledVisita?.id_quinta
                    })
                )
                if (prefilledVisita!!._blocked) binding.spinnerQuinta.isEnabled = false
            }
            if (prefilledVisita!!.id_tecnico != null) {
                visita.id_tecnico = prefilledVisita?.id_tecnico!!
                binding.spinnerTecnico.setSelection(
                    usuariosAdapter.getPosition(tecnicos.find {
                        it.id_user == prefilledVisita?.id_tecnico
                    })
                )
                if (prefilledVisita!!._blocked) binding.spinnerTecnico.isEnabled = false
            }
            if (!prefilledVisita!!.parcelas.isNullOrEmpty()) {
                visita.parcelas.clear()
                visita.parcelas.addAll(prefilledVisita!!.parcelas!!)
                parcelasAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun commitChange() =
        returnSimpleApiCall(
            { VisitaApi().postVisita(visita) },
            "Hubo un error. La visita no pudo ser creada."
        )

    override fun denyAction() {
        activity?.onBackPressed()
    }
}