package com.jm.appsolve_fe

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class LoginSignUpPasswordActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_sign_up_password)

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

        val etpassword1 = findViewById<EditText>(R.id.etpassword1)
        val check1 = findViewById<ImageView>(R.id.check1)
        val enCheck = findViewById<EditText>(R.id.enCheck)
        val check2 = findViewById<ImageView>(R.id.check2)
        val numCheck = findViewById<EditText>(R.id.numCheck)
        val check3 = findViewById<ImageView>(R.id.check3)
        val countCheck = findViewById<EditText>(R.id.countCheck)

        etpassword1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val password = s.toString()

                // 1. 영문 입력 여부 확인
                if (password.any{ it.isLetter()}) {
                    check1.setImageResource(R.drawable.check2)
                    enCheck.setTextColor(getColor(R.color.purple))
                } else {
                    check1.setImageResource(R.drawable.check)
                    enCheck.setTextColor(R.color.gray_9E9E9E)
                }

                // 2. 숫자 입력 여부 확인
                if (password.any { it.isDigit() }) {
                    check2.setImageResource(R.drawable.check2)
                    numCheck.setTextColor(getColor(R.color.purple))
                } else {
                    check2.setImageResource(R.drawable.check)
                    numCheck.setTextColor(getColor(R.color.gray_9E9E9E))
                }

                // 3. 글자 수 확인
                if (password.length in 8..20) {
                    check3.setImageResource(R.drawable.check2)
                    countCheck.setTextColor(getColor(R.color.purple))
                } else {
                    check3.setImageResource(R.drawable.check)
                    countCheck.setTextColor(getColor(R.color.gray_9E9E9E))
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


    }
}