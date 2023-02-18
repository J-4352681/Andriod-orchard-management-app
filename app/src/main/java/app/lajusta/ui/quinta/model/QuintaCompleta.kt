package app.lajusta.ui.quinta.model

import android.os.Parcelable
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.quinta.Quinta
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuintaCompleta(
    val id_quinta: Int,
    val nombre: String,
    val direccion: String?,
    val geoImg: String?,
    val familia: Familia,
): Parcelable {
    fun toQuinta(): Quinta = Quinta(id_quinta, nombre, direccion, geoImg, familia.id_fp)

    fun toQuintaCompleta(quinta: Quinta, familia: Familia): QuintaCompleta =
        QuintaCompleta(
            quinta.id_quinta,
            quinta.nombre,
            quinta.direccion,
            quinta.geoImg,
            familia
        )
}
