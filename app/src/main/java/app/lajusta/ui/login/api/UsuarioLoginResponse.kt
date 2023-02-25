package app.lajusta.ui.login.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UsuarioLoginResponse (
    val id_user:Int,
    val rol:Int,
    val token:String
): Parcelable