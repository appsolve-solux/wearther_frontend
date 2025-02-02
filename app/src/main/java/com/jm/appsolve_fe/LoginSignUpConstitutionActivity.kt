package com.jm.appsolve_fe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LoginSignUpConstitutionActivity : AppCompatActivity() {

    private var selectedConstitution: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_sign_up_constitution)

        val backBtn = findViewById<ImageView>(R.id.backBtn)

        backBtn.setOnClickListener{
            val intent = Intent(this, LoginSignUpLocationActivity::class.java)
            startActivity(intent)
            finish()
        }

        val closeBtn = findViewById<ImageView>(R.id.closeBtn)

        closeBtn.setOnClickListener {
            finishAffinity()
        }

        val hotConstitution = findViewById<TextView>(R.id.hotConstitution)
        val coldConstitution = findViewById<TextView>(R.id.coldConstitution)
        val bothConstitution = findViewById<TextView>(R.id.bothConstitution)
        val constitutionBtnNext = findViewById<Button>(R.id.constitutionBtnNext)

        setConstitutionClickListener(hotConstitution, constitutionBtnNext, 0)
        setConstitutionClickListener(coldConstitution, constitutionBtnNext, 1)
        setConstitutionClickListener(bothConstitution, constitutionBtnNext, 2)

        constitutionBtnNext.setOnClickListener {
            Log.d("LoginPickTopActivity", "다음 버튼 클릭됨")
            val intent = Intent(this, LoginPickTopActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setConstitutionClickListener(constitution: TextView, button: Button, value: Int) {
        constitution.setOnClickListener {
            if (selectedConstitution == constitution) {
                resetSelection(constitution)
                selectedConstitution = null
                updateButtonState(button, false)
            } else {
                selectedConstitution?.let { resetSelection(it) }
                selectedConstitution(constitution)
                selectedConstitution = constitution
                SignUpDataStore.saveData(this, "constitution", value.toString())
                updateButtonState(button, true)
            }
        }
    }

    private fun resetSelection(constitution: TextView) {
        constitution.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        constitution.setTextColor(ContextCompat.getColor(this, R.color.black))
        constitution.setBackgroundResource(R.drawable.rounded_edittext_background)
    }

    private fun selectedConstitution(constitution: TextView) {
        constitution.setBackgroundColor(ContextCompat.getColor(this, R.color.purple))
        constitution.setTextColor(ContextCompat.getColor(this, R.color.white))
        constitution.setBackgroundResource(R.drawable.rounded_edittext_selected_background)
    }

    private fun updateButtonState(button: Button, isEnabled: Boolean) {
        if (isEnabled) {
            button.isEnabled = true
            button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple))
        } else {
            button.isEnabled = false
            button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray_9E9E9E))
        }
    }
}