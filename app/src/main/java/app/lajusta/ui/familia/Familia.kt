package app.lajusta.ui.familia

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Familia(
    var id_fp: Int,
    var nombre: String,
    var fecha_afiliacion: List<Int>
): Parcelable {
    override fun toString(): String = nombre

    fun toPrefilledFamilia() = PrefilledFamilia(nombre, fecha_afiliacion)
    fun toBlockedPrefilledFamilia() =
        PrefilledFamilia(nombre, fecha_afiliacion, true, true)
}

@Parcelize
data class PrefilledFamilia(
    var nombre: String? = null,
    var fecha_afiliacion: List<Int>? = null,
    var _blockFields: Boolean = false,
    var _blockSubmitAction: Boolean = false
): Parcelable
