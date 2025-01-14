package com.jm.appsolve_fe

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LoginSignUpLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_sign_up_location)

        val backBtn = findViewById<ImageView>(R.id.backBtn)

        backBtn.setOnClickListener{
            val intent = Intent(this, LoginSignUpPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        val closeBtn = findViewById<ImageView>(R.id.closeBtn)

        closeBtn.setOnClickListener {
            finishAffinity()
        }

        val locationSearchEditText = findViewById<EditText>(R.id.locationSearchEditText)
        val locationBtnNext = findViewById<Button>(R.id.locationBtnNext)

        locationSearchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonState(locationBtnNext, s.toString().isNotEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        locationSearchEditText.setOnClickListener {
            val bottomSheet = LoginLocationBottomSheetActivity()
            bottomSheet.setOnLocationSelectedListener { selectedLocation ->
                locationSearchEditText.setText(selectedLocation)
                updateButtonState(locationBtnNext, selectedLocation.isNotEmpty())
            }
            bottomSheet.show(supportFragmentManager, "LoginLocationBottomSheetActivity")
        }

        locationBtnNext.setOnClickListener {
            val intent = Intent(this, LoginSignUpConstitutionActivity::class.java)
            startActivity(intent)
        }
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