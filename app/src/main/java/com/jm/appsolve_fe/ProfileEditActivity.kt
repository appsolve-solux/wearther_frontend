package com.jm.appsolve_fe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ProfileEditActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        val backBtn = findViewById<ImageView>(R.id.backBtn)

        backBtn.setOnClickListener{
//            val intent = Intent(this, Mypage::class.java)
//            startActivity(intent)
            finish()
        }

        val pwNext = findViewById<ImageView>(R.id.pwNext)

        pwNext.setOnClickListener{
            val intent = Intent(this, PasswordEditActivity::class.java)
            startActivity(intent)
            finish()
        }

        val constitutionNext = findViewById<ImageView>(R.id.constitutionNext)

        constitutionNext.setOnClickListener{
            val intent = Intent(this, ConstitutionEditActivity::class.java)
            startActivity(intent)
            finish()
        }

        val preferNext = findViewById<ImageView>(R.id.preferNext)

        preferNext.setOnClickListener{
            val intent = Intent(this, PreferEditActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}