package app.lajusta.ui.Parcela

import android.os.Parcelable
import app.lajusta.ui.verdura.Verdura
import kotlinx.parcelize.Parcelize

@Parcelize
data class Parcela(
    val id_parcela: Int,
    val cantidad_surcos: Int,
    val cubierta: Boolean,
    val cosecha: Boolean,
    val id_visita: Int,
    var verduras: Verdura
): Parcelable
