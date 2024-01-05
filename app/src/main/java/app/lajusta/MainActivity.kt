package app.lajusta

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.lajusta.data.Preferences.PreferenceHelper.clearValues
import app.lajusta.data.Preferences.PreferenceHelper.loggedIn
import app.lajusta.data.Preferences.PreferenceHelper.userType
import app.lajusta.databinding.ActivityMainBinding
import app.lajusta.ui.login.LoginActivity
import app.lajusta.ui.usuarios.UserRole
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    val CUSTOM_PREF_NAME = "User_data"
    lateinit var prefs: SharedPreferences

    companion object {


        // SET YOUR IP HERE -----------------------------------------------
        const val baseIP: String = "192.168.0.120"
        // SET YOUR IP HERE -----------------------------------------------


        const val baseUrl: String = "http://"+baseIP+":80/api/"
        const val LOGIN_DEBUG = true //Permite usuarios "Administrador Administrador", "Tecnico Tecnico" y "aaaaaa aaaaaa" para entrar por defecto a la app
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = applicationContext.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)

        //CHECKING LOGIN
        if ( !prefs.loggedIn ) goToLogin()

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration =
            UserRole.getByRoleId(prefs.userType).getAppBarConfiguration(drawerLayout)

        binding.navView.menu.clear()
        binding.navView.inflateMenu(UserRole.getByRoleId(prefs.userType).getMainDrawerMenu())

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
                return true
            }
            R.id.action_logout -> {
                val prefs = applicationContext.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)
                prefs.clearValues()
                prefs.loggedIn = false
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


}
