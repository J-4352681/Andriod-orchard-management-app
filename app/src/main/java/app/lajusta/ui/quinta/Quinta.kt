package app.lajusta.ui.quinta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quinta(
    val id_quinta: Int,
    var nombre: String,
    var direccion: String?,
    var geoImg: String?,
    var fpId: Int,
): Parcelable {
    override fun toString(): String = nombre

    fun toPrefilledQuinta() =
        PrefilledQuinta(nombre, direccion, geoImg, fpId)

    fun toBlockedPrefilledQuinta() =
        PrefilledQuinta(nombre, direccion, geoImg, fpId, true)
}

@Parcelize
data class PrefilledQuinta(
    var nombre: String? = null,
    var direccion: String? = null,
    var geoImg: String? = null,
    var fpId: Int? = null,
    val _block: Boolean = false
): Parcelable