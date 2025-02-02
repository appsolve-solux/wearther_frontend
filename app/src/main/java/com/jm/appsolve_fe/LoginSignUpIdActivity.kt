package com.jm.appsolve_fe

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jm.appsolve_fe.network.RetrofitClient
import com.jm.appsolve_fe.network.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginSignUpIdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_sign_up_id)

        val backBtn = findViewById<ImageView>(R.id.backBtn)

        backBtn.setOnClickListener{
            val intent = Intent(this, LoginSignUpEmailActivity::class.java)
            startActivity(intent)
            finish()
        }

        val closeBtn = findViewById<ImageView>(R.id.closeBtn)

        closeBtn.setOnClickListener {
            finishAffinity()
        }

        val btnDupCheck = findViewById<Button>(R.id.btnDupCheck)
        val etId = findViewById<EditText>(R.id.etId)
        val idBtnNext = findViewById<Button>(R.id.idBtnNext)
        val tvDupResult = findViewById<TextView>(R.id.tvDupResult)

        btnDupCheck.isEnabled = false
        idBtnNext.isEnabled = false

        etId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val idInput = s.toString()
                btnDupCheck.isEnabled = idInput.isNotEmpty()
                tvDupResult.visibility = View.GONE
                idBtnNext.isEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnDupCheck.setOnClickListener {
            val idInput = etId.text.toString()
            checkIdDuplication(idInput, etId, tvDupResult, idBtnNext)
        }

        idBtnNext.setOnClickListener {
            val idInput = etId.text.toString()

            SignUpDataStore.saveData(this, "loginId", idInput)

            val intent = Intent(this, LoginSignUpPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkIdDuplication(
        idInput: String,
        etId: EditText,
        tvDupResult: TextView,
        idBtnNext: Button
    ) {
        RetrofitClient.signUpService.checkDuplicate(idInput).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val isDuplicated = response.body()?.result?.isDuplicated?: false
                    if (isDuplicated) {
                        tvDupResult.text = "이미 존재하는 아이디입니다."
                        tvDupResult.setTextColor(Color.RED)
                        tvDupResult.visibility = View.VISIBLE
                        etId.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.passwordwrong,
                            0
                        )
                        idBtnNext.isEnabled = false
                    } else {
                        tvDupResult.text = "사용 가능한 아이디입니다."
                        tvDupResult.setTextColor(ContextCompat.getColor(this@LoginSignUpIdActivity, R.color.purple))
                        tvDupResult.visibility = View.VISIBLE
                        etId.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.passwordtrue,
                            0
                        )
                        idBtnNext.isEnabled = true
                    }
                } else {
                    tvDupResult.text = "아이디 중복 확인에 실패했습니다."
                    tvDupResult.setTextColor(Color.RED)
                    tvDupResult.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Network error: ${t.message}", t)
                tvDupResult.text = "네트워크 오류가 발생했습니다."
                tvDupResult.setTextColor(Color.RED)
                tvDupResult.visibility = View.VISIBLE
            }
        })
    }
}