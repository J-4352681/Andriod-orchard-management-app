package app.lajusta.ui.usuarios

import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import app.lajusta.R
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.ronda.PrefilledRonda
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.visita.PrefilledVisita
import app.lajusta.ui.visita.Visita

enum class UserRole(
    private val roleId: Int
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

        override fun goToModificationRonda(navController: NavController, ronda: Ronda) =
            navController.navigate(R.id.rondaModifyFragment, bundleOf("ronda" to ronda))

        override fun goToCreationRonda(navController: NavController) =
            navController.navigate(R.id.rondaCreateFragment)
    },

    TECNICO(1) {
        override fun getAppBarConfiguration(
            drawerLayout: DrawerLayout
        ): AppBarConfiguration =
            AppBarConfiguration(
                setOf(R.id.nav_familias, R.id.nav_bolson,
                    R.id.nav_quintas, R.id.nav_verduras,
                    R.id.nav_visitas
                ), drawerLayout
            )

        override fun getMainDrawerMenu(): Int = R.menu.activity_main_drawer_tecnico

        override fun goToModificationRonda(navController: NavController, ronda: Ronda) =
            navController.navigate(R.id.rondaModifyFragment, bundleOf(
                "prefilledRonda" to ronda.toBlockedPrefilledRonda()
            ))

        override fun goToCreationRonda(navController: NavController) =
            navController.navigate(R.id.rondaCreateFragment, bundleOf(
                "prefilledRonda" to PrefilledRonda(
                    ArrayedDate.todayArrayed(), ArrayedDate.todayArrayed(),
                    true, true
                )))
    };

    abstract fun getAppBarConfiguration(drawerLayout: DrawerLayout): AppBarConfiguration
    abstract fun getMainDrawerMenu(): Int

    // BOLSONES
    // abstract fun goToModificationBolson(bolson: Bolson)
    // abstract fun goToCreationBolson(bolson: Bolson)

    // VISITAS
    fun goToModificationVisita(userId: Int, visita: Visita, navController: NavController) {
        val bundle = bundleOf(
            "visita" to visita,
            "prefilledVisita" to PrefilledVisita(id_tecnico = userId, _blockFields = true)
        )
        navController.navigate(R.id.visitaModifyFragment, bundle)
    }
    fun goToVisitaCreation(userId: Int, navController: NavController, visita: Visita? = null) {
        val pv = "prefilledVisita" to PrefilledVisita(id_tecnico = userId, _blockFields = true)
        val v = "visita" to visita
        val bundle = if (visita != null) bundleOf(pv, v) else bundleOf(pv)
        navController.navigate(R.id.visitasCreateFragment, bundle)
    }

    // RONDAS
    abstract fun goToModificationRonda(navController: NavController, ronda: Ronda)
    abstract fun goToCreationRonda(navController: NavController)

    companion object {
        private val roles = values().associateBy( { it.roleId }, { it } )

        fun getByRoleId(roleId: Int): UserRole = roles[roleId]!!
    }
}