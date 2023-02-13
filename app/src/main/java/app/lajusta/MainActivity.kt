package app.lajusta

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.lajusta.data.model.UserType
import app.lajusta.databinding.ActivityMainBinding
import app.lajusta.ui.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val baseUrl: String = "http://192.168.0.15:80/api/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setup

        //if ( (!intent.hasExtra("userType")) || (!intent.hasExtra("displayName"))) {
        //    goToLogin()
        //}
        val displayName = intent.getStringExtra("displayName")
        val userType = intent.getStringExtra("userType")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            //Voy a usar esto para comprobar si mandan los datos correctos del login:
            if (userType == UserType.ADMIN.toString()) {
                Snackbar.make(view, "Tienes permiso de administrador", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                Snackbar.make(view, "displayName: $displayName userType: $userType", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        //TODO Hacer el ABM de usuarios
        if (userType == UserType.ADMIN.toString()) {
            //MOSTRAR EL ABM DE USUARIOS
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_rondas, R.id.nav_visitas, R.id.nav_familias, R.id.nav_bolson
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(applicationContext, "click on Profile", Toast.LENGTH_LONG).show()
                val i = Intent(applicationContext, ProfileActivity::class.java)
                i.putExtra("displayName", intent.getStringExtra("displayName"))
                i.putExtra("userType", intent.getStringExtra("userType"))
                startActivity(i)
                true
            }
            R.id.action_profile ->{
                Toast.makeText(applicationContext, "click on Logout", Toast.LENGTH_LONG).show()
                goToLogin()
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