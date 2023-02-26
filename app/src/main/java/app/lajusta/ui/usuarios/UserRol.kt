package app.lajusta.ui.usuarios

import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import app.lajusta.R

enum class UserRol(
    private val rolId: Int
) {
    ADMIN(0) {
        override fun getAppBarConfiguration(
            drawerLayout: DrawerLayout
        ): AppBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.nav_rondas, R.id.nav_visitas,
                    R.id.nav_familias, R.id.nav_bolson,
                    R.id.nav_quintas, R.id.nav_verduras,
                    R.id.nav_usuarios
                ), drawerLayout
            )

        override fun getMainDrawerMenu(): Int = R.menu.activity_main_drawer_admin
    },

    TECNICO(1) {
        override fun getAppBarConfiguration(
            drawerLayout: DrawerLayout
        ): AppBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.nav_rondas, R.id.nav_visitas,
                    R.id.nav_familias, R.id.nav_bolson,
                    R.id.nav_quintas, R.id.nav_verduras
                ), drawerLayout
            )

        override fun getMainDrawerMenu(): Int = R.menu.activity_main_drawer_tecnico
    };

    fun getRolId() = rolId

    abstract fun getAppBarConfiguration(drawerLayout: DrawerLayout): AppBarConfiguration
    abstract fun getMainDrawerMenu(): Int

    companion object {
        fun getUserRolByRolId(rolId: Int): UserRol? = values().firstOrNull { it.rolId == rolId }
    }
}