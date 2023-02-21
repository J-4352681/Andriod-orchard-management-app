package app.lajusta.ui.usuarios

import android.os.Parcelable
import app.lajusta.data.model.UserType
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.parcela.Parcela
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.ronda.Ronda
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Usuario(
    var id_user: Int,
    var nombre: String,
    var apellido: String,
    var direccion: String?,
    var username: String,
    var password: String,       //Como hacer que haya un poco de seguridad aca?
    var email: String,
    val roles: Int,
) : Parcelable {

    companion object {

        val adminName = "Administrador/a"
        val tecName = "Tecnico/a"
        val adminNumber = 0
        val tecNumber = 1
        fun filter(
            usuarios: MutableList<Usuario>,
            usuariosOriginales: MutableList<Usuario>,
            query: String?
        ): MutableList<Usuario> {
            usuarios.clear()
            if (query.isNullOrEmpty()) usuarios.addAll(usuariosOriginales)
            else usuarios.addAll(
                usuariosOriginales.filter { usuarios ->
                    usuarios.nombre.contains(query)
                            || usuarios.apellido.contains(query)
                }
            )
            return usuarios
        }

        fun getRolNames():Array<String> {
            return arrayOf(adminName,tecName)
        }

        fun rolNameToNumber( name:String ):Int {
            if (name == adminName) return adminNumber
            else if (name == tecName) return tecNumber
            return -1 //UNKNOWN
        }

        fun rolNumberToUserType(num: Int?): UserType? {
            if (num == adminNumber) return UserType.ADMIN
            else if (num == tecNumber) return UserType.TECNICO
            return null
        }

        fun rolNumberToName(num: Int): String {
            if (num == adminNumber) return adminName
            else if (num == tecNumber) return tecName
            return "UNKNOWN"
        }
    }
}