package app.lajusta.ui.parcela

import android.os.Parcelable
import app.lajusta.ui.verdura.Verdura
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelaVisita(
    val id_parcela: Int,
    val cantidad_surcos: Int,
    val cubierta: Boolean,
    val cosecha: Boolean,
    var verdura: Verdura
): Parcelable
