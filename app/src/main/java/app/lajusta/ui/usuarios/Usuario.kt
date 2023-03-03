package app.lajusta.ui.usuarios

import android.content.Context
import android.os.Parcelable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import app.lajusta.R
import app.lajusta.data.model.UserType
import app.lajusta.ui.quinta.model.QuintaCompleta
import kotlinx.parcelize.Parcelize

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
) : Parcelable, Comparable<Usuario> {

    override fun compareTo(other: Usuario): Int {
        return if ( apellido.compareTo(other.apellido) == 0 ) nombre.compareTo(other.nombre)
        else apellido.compareTo(other.apellido)
    }

    override fun toString(): String = nombre

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

        fun getRolNames(): Array<String> {
            return arrayOf(adminName, tecName)
        }

        fun rolNameToNumber(name: String): Int {
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

        fun isAdmin(num: Int): Boolean {
            return num == adminNumber
        }

        fun isAdmin(name: String): Boolean {
            return name == adminName
        }

        fun getAppBarConfiguration(num: Int, drawerLayout: DrawerLayout): AppBarConfiguration {
            when (num) {
                adminNumber -> {
                    return AppBarConfiguration(
                        setOf(
                            R.id.nav_rondas, R.id.nav_visitas,
                            R.id.nav_familias, R.id.nav_bolson,
                            R.id.nav_quintas, R.id.nav_verduras,
                            R.id.nav_usuarios
                        ), drawerLayout
                    )
                }
                tecNumber -> {
                    return AppBarConfiguration(
                        setOf(
                            R.id.nav_rondas, R.id.nav_visitas,
                            R.id.nav_familias, R.id.nav_bolson,
                            R.id.nav_quintas, R.id.nav_verduras
                        ), drawerLayout
                    )
                }
                else -> {
                    return AppBarConfiguration(
                        setOf(), drawerLayout
                    )
                }
            }
        }
    }
}