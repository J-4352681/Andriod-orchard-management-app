package app.lajusta.ui.quinta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quinta(
    val id_quinta: Int,
    val nombre: String?,
    val direccion: String?,
    val geoImg: String?,
    val fpId: Int,
): Parcelable
