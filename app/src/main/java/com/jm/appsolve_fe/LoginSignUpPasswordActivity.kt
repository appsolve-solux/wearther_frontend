package com.jm.appsolve_fe

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LoginSignUpPasswordActivity : AppCompatActivity() {

    private var isPasswordValid = false
    private var isPasswordMatched = false

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
        val etpassword2 = findViewById<EditText>(R.id.etpassword2)
        val check1 = findViewById<ImageView>(R.id.check1)
        val enCheck = findViewById<TextView>(R.id.enCheck)
        val check2 = findViewById<ImageView>(R.id.check2)
        val numCheck = findViewById<TextView>(R.id.numCheck)
        val check3 = findViewById<ImageView>(R.id.check3)
        val countCheck = findViewById<TextView>(R.id.countCheck)
        val passwordbtnNext = findViewById<Button>(R.id.passwordBtnNext)

        val passwordErrorText = findViewById<TextView>(R.id.passwordErrorText)

        etpassword1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val password = s.toString()
                val password1 = etpassword1.text.toString()

                // 1. 영문 입력 여부 확인
                if (password.any{ it.isLetter()}) {
                    check1.setImageResource(R.drawable.check2)
                    enCheck.setTextColor(getColor(R.color.purple))
                } else {
                    check1.setImageResource(R.drawable.check)
                    enCheck.setTextColor(getColor(R.color.gray_9E9E9E))
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



                // 비밀번호 조건 유효성 확인
                isPasswordValid = password.any { it.isLetter() } &&
                        password.any { it.isDigit() } &&
                        password.length in 8..20

                updateButtonState(passwordbtnNext, isPasswordValid, isPasswordMatched)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 비밀번호2 입력 처리
        etpassword2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val confirmPassword = s.toString()

                if (confirmPassword.isNotEmpty()) {
                    if (confirmPassword == etpassword1.text.toString()) {
                        // 비밀번호가 일치하는 경우
                        passwordErrorText.visibility = View.GONE
                        etpassword2.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.passwordtrue,
                            0
                        )
                        isPasswordMatched = true
                    } else {
                        // 비밀번호가 일치하지 않는 경우
                        passwordErrorText.visibility = View.VISIBLE
                        passwordErrorText.text = "비밀번호가 일치하지 않습니다. 다시 입력해주세요."
                        passwordErrorText.setTextColor(ContextCompat.getColor(this@LoginSignUpPasswordActivity, R.color.red))
                        etpassword2.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.passwordwrong,
                            0
                        )
                        isPasswordMatched = false
                    }
                } else {
                    // 재입력 필드가 비어 있을 경우
                    passwordErrorText.visibility = View.GONE
                    etpassword2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    isPasswordMatched = false
                }

                // 버튼 상태 업데이트
                updateButtonState(passwordbtnNext, isPasswordValid, isPasswordMatched)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 다음 버튼 클릭 처리
        passwordbtnNext.setOnClickListener {
            val intent = Intent(this,LoginSignUpLocationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateButtonState(button: Button, isValid: Boolean, isMatched: Boolean) {
        if (isValid && isMatched) {
            button.isEnabled = true
            button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple))
        } else {
            button.isEnabled = false
            button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray_9E9E9E))
        }
    }
}