package app.lajusta

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.lajusta.data.model.UserType
import app.lajusta.databinding.ActivityMainBinding
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.login.LoginActivity
import app.lajusta.ui.usuarios.Usuario
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object {
        //const val baseUrl: String = "http://163.10.141.61:80/api/" // LABO
        //const val baseUrl: String = "http://192.168.0.15:80/api/" // TOMI
        //const val baseUrl: String = "http://192.168.0.120:80/api/" // JERE
        const val baseUrl: String = "http://192.168.0.254:80/api/" // JERE
        var userId: Int? = 1
        var userName: String = "juanPerez"
        var userType: Int? = 1
        var token: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //CHECKING LOGIN
        /* if ( userName.isEmpty() || userType.isEmpty() || userId.isEmpty() || token.isEmpty() ) {
            goToLogin()
        } */

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        //TODO Hacer el ABM de usuarios
        //if (Usuario.isAdmin(userType)) {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_rondas, R.id.nav_visitas,
                    R.id.nav_familias, R.id.nav_bolson,
                    R.id.nav_quintas, R.id.nav_verduras,
                    R.id.nav_usuarios
                ), drawerLayout
            )
        /*} else {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_rondas, R.id.nav_visitas,
                    R.id.nav_familias, R.id.nav_bolson,
                    R.id.nav_quintas, R.id.nav_verduras
                ), drawerLayout
            )
        }*/
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                val i = Intent(applicationContext, ProfileActivity::class.java)
                startActivity(i)
                true
            }
            R.id.action_logout -> {
                userId = null
                userName = ""
                userType = null
                token = ""
                goToLogin()
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun goToLogin() {
        val i = Intent(applicationContext, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun goToGenericMap() {
        val i = Intent(applicationContext, QuintaMapaActivity::class.java)
        startActivity(i)
    }
}