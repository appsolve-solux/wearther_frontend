package com.jm.appsolve_fe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class LoginSignUpLocationActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_sign_up_location)

        Log.d("LoginSignUpLocationActivity", "onCreate 호출됨")

        val backBtn = findViewById<ImageView>(R.id.backBtn)

        backBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val closeBtn = findViewById<ImageView>(R.id.closeBtn)

        closeBtn.setOnClickListener {
            finishAffinity()
        }

        val locationSearchEditText = findViewById<EditText>(R.id.locationSearchEditText)

        locationSearchEditText.setOnClickListener {
            val bottomSheet = LoginLocationBottomSheetActivity()
            bottomSheet.show(supportFragmentManager, "LoginLocationBottomSheetActivity")
        }

    }
}