package app.lajusta.ui.quinta.modify

import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import app.lajusta.R
import app.lajusta.data.Preferences.PreferenceHelper.userId
import app.lajusta.data.Preferences.PreferenceHelper.userType
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.quinta.api.QuintaApi
import app.lajusta.ui.quinta.edition.QuintaBaseEditionFragment
import app.lajusta.ui.usuarios.UserRole

class QuintaModifyFragment: QuintaBaseEditionFragment() {
    override fun withApiData() {
        initAddVisita()
        initGoToMap()
    }

    private fun initAddVisita() {
        val bAddVisita = Button(activity).also {
            it.text = "Agregar visita"
            it.id = View.generateViewId()
        }
        binding.clContainer.addView(bAddVisita)
        val set = ConstraintSet()
        set.clone(binding.clContainer)
        bAddVisita.id.also {
            set.connect(it, ConstraintSet.START, binding.clContainer.id, ConstraintSet.START)
            set.connect(it, ConstraintSet.TOP, binding.sFamilia.id, ConstraintSet.BOTTOM)
        }
        set.applyTo(binding.clContainer)
        bAddVisita.setOnClickListener {
            if (visitas.isNotEmpty()) {
                val visita = visitas
                    .filter { it.id_quinta == quinta.id_quinta }
                    .maxBy { ArrayedDate.toDate(it.fecha_visita) }
                UserRole.getByRoleId(prefs.userType).goToVisitaCreation(
                    prefs.userId, findNavController(), visita
                )
            }
        }
    }

    private fun initGoToMap() {
        val bGoToMap = Button(activity).also {
            it.text = "Ver en mapa"
            it.id = View.generateViewId()
        }
        binding.clContainer.addView(bGoToMap)
        val set = ConstraintSet()
        set.clone(binding.clContainer)
        bGoToMap.id.also {
            set.connect(it, ConstraintSet.END, binding.clContainer.id, ConstraintSet.END)
            set.connect(it, ConstraintSet.TOP, binding.sFamilia.id, ConstraintSet.BOTTOM)
        }
        set.applyTo(binding.clContainer)
        bGoToMap.setOnClickListener {
            val bundle = bundleOf("quinta" to quinta)
            this.findNavController().navigate(R.id.quintaMapaFragment, bundle)
        }
    }

    override fun denyAction() {
        returnSimpleApiCall(
            { QuintaApi().deleteQuinta(quinta.id_quinta) },
            "Hubo un error. La quinta no pudo ser eliminada."
        )
    }

    override fun commitChange() {
        returnSimpleApiCall(
            { QuintaApi().putQuinta(quinta.id_quinta, quinta) },
            "Hubo un error. La quinta no pudo ser modificada."
        )
    }
}