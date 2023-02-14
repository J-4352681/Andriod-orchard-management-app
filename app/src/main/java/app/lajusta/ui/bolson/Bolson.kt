package app.lajusta.ui.bolson

import android.os.Parcelable
import app.lajusta.ui.verdura.Verdura
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bolson(
    val cantidad: Int,
    val idFp: Int,
    val idRonda: Int,
    val id_bolson: Int,
    // val verduras: List<Verdura>
): Parcelable
