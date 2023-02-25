package app.lajusta.ui.login.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UsuarioLogin (
    var username:String,
    var password:String
): Parcelable {
    override fun toString(): String = "$username - $password"
}