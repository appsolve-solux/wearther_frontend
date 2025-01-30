package com.jm.appsolve_fe

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jm.appsolve_fe.network.LoginRequest
import com.jm.appsolve_fe.network.LoginResponse
import com.jm.appsolve_fe.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val loginId = findViewById<EditText>(R.id.loginId)
        val loginPassword = findViewById<EditText>(R.id.loginPassword)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.isEnabled = false
        loginBtn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gray_9E9E9E)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateLoginButtonState(loginId, loginPassword, loginBtn)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        loginId.addTextChangedListener(textWatcher)
        loginPassword.addTextChangedListener(textWatcher)

        loginBtn.setOnClickListener {
            val loginId = loginId.text.toString()
            val loginPassword = loginPassword.text.toString()
            val deviceId = Build.MODEL

            loginUser(loginId, loginPassword, deviceId)
        }
    }

    private fun updateLoginButtonState(loginId: EditText, loginPassword: EditText, loginBtn: Button) {
        val isIdNotEmpty = loginId.text.isNotEmpty()
        val isPwNotEmpty = loginPassword.text.isNotEmpty()

        if (isIdNotEmpty && isPwNotEmpty) {
            loginBtn.isEnabled = true
            loginBtn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.purple)
        } else {
            loginBtn.isEnabled = false
            loginBtn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gray_9E9E9E)
        }
    }

    private fun loginUser(loginId: String, loginPassword: String, deviceId: String) {
        val request = LoginRequest(loginId, loginPassword, deviceId)
        RetrofitClient.apiService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    // loginResponse?.result가 null이 아니어야 실행
                    loginResponse?.result?.let { result ->
                        if (loginResponse.success) {
                            // 토큰 저장
                            SignUpDataStore.saveData(this@LoginActivity, "accessToken", result.accessToken ?: "")
                            SignUpDataStore.saveData(this@LoginActivity, "refreshToken", result.refreshToken ?: "")
                            SignUpDataStore.saveData(this@LoginActivity, "memberId", result.memberId?.toString() ?: "0")

                            // 메인 화면 이동
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    } ?: run {
                        Toast.makeText(this@LoginActivity, "서버 응답이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}