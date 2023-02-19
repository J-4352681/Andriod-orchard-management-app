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
): Parcelable
