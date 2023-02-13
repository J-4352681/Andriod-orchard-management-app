package app.lajusta

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //Setup
        val displayName = intent.getStringExtra("displayName")
        val userType = intent.getStringExtra("userType")

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Perfil"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Username
        val editText = findViewById<EditText>(R.id.editTextUsername)
        editText.hint = displayName

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}