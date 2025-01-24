package com.jm.appsolve_fe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jm.appsolve_fe.network.RetrofitClient
import com.jm.appsolve_fe.network.SignUpRequest
import com.jm.appsolve_fe.network.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginSignUpEmailActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_signup_email)

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

        val spinnerEmailDomain = findViewById<Spinner>(R.id.spinnerEmailDomain)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val emailBtnNext = findViewById<Button>(R.id.emailBtnNext)

        val emailDomains = resources.getStringArray(R.array.email_domains).toList()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            emailDomains
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEmailDomain.adapter = adapter

        // Spinner 선택 상태 변수
        var selectedDomainValid = false

        // Spinner 이벤트 처리
        spinnerEmailDomain.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDomainValid = position != 0
                updateButtonState(etEmail.text.toString(), selectedDomainValid, emailBtnNext)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedDomainValid = false
                updateButtonState(etEmail.text.toString(), selectedDomainValid, emailBtnNext)
            }
        }

        // EditText 입력 상태 확인
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonState(s.toString(), selectedDomainValid, emailBtnNext)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 버튼 클릭 이벤트 처리
        emailBtnNext.setOnClickListener {
            if (emailBtnNext.isEnabled) {
                val email = etEmail.text.toString()
                val domain = spinnerEmailDomain.selectedItem.toString()
                val fullEmail = "$email@$domain"

                SignUpDataStore.saveData(this, "email", fullEmail)

                val intent = Intent(this, LoginSignUpIdActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 버튼 활성화 상태 업데이트
    private fun updateButtonState(email: String, isDomainValid: Boolean, button: Button) {
        if (email.isNotEmpty() && isDomainValid) {
            button.isEnabled = true
            button.setBackgroundTintList(getColorStateList(R.color.purple))
        } else {
            button.isEnabled = false
            button.setBackgroundTintList(getColorStateList(R.color.gray_9E9E9E))
        }
    }

}