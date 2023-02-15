package app.lajusta.ui.bolson

import android.os.Parcelable
import app.lajusta.ui.verdura.Verdura
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bolson(
    val id_bolson: Int,
    var cantidad: Int,
    var idFp: Int,
    var idRonda: Int,
    var verduras: List<Verdura>
): Parcelable
