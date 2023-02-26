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
    val verduras: MutableList<Verdura>
): Parcelable {
    fun toPrefilledBolson() =
        PrefilledBolson(id_bolson, cantidad, idFp, idRonda, verduras)

    fun toBlockedPrefilledBolson() =
        PrefilledBolson(id_bolson, cantidad, idFp, idRonda, verduras, true)
}


@Parcelize
data class PrefilledBolson(
    val id_bolson: Int? = null,
    var cantidad: Int? = null,
    var idFp: Int? = null,
    var idRonda: Int? = null,
    val verduras: MutableList<Verdura>? = null,
    val _block: Boolean = false
): Parcelable