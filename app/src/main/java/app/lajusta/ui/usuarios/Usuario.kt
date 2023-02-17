package app.lajusta.ui.usuarios

import android.os.Parcelable
import app.lajusta.ui.parcela.Parcela
import app.lajusta.ui.parcela.ParcelaVisita
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Usuario(
    val id_user: Int,
    var nombre: String,
    var apellido: String,
    var direccion: String?,
    var username: String,
    var password: String,       //Como hacer que haya un poco de seguridad aca?
    var email: String,
    val roles: Int,
): Parcelable