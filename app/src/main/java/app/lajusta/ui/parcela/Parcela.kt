package app.lajusta.ui.parcela

import android.os.Parcelable
import app.lajusta.ui.verdura.Verdura
import kotlinx.parcelize.Parcelize

@Parcelize
data class Parcela(
    val id_parcela: Int,
    var cantidad_surcos: Int,
    var cubierta: Boolean,
    var cosecha: Boolean,
    var id_visita: Int,
    var id_verdura: Verdura
): Parcelable
