package app.lajusta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.lajusta.ui.login.LoginActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }
}
