package app.lajusta.ui.login.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UsuarioLogin (
    val username:String,
    val password:String

        ): Parcelable