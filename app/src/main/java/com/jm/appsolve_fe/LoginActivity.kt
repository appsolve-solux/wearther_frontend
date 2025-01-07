package com.jm.appsolve_fe

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_login)

        val backBtn = findViewById<ImageView>(R.id.backBtn)

        backBtn.setOnClickListener{
            finishAffinity()
        }

        val closeBtn = findViewById<ImageView>(R.id.closeBtn)

        closeBtn.setOnClickListener {
            finishAffinity()
        }

        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)

        tvSignUp.setOnClickListener {
            val spannableText = SpannableString(tvSignUp.text.toString())
            spannableText.setSpan(UnderlineSpan(), 0, spannableText.length, 0)
            tvSignUp.text = spannableText
            tvSignUp.setTextColor(getColor(R.color.purple))

            val intent = Intent(this, LoginSignUpEmailActivity::class.java)
            startActivity(intent)
        }
    }
}