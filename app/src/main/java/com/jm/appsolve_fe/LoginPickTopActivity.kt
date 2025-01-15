package com.jm.appsolve_fe

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout

class LoginPickTopActivity : AppCompatActivity() {

    private val pickTopList = listOf(
        "민소매", "반소매", "니트", "맨투맨, 후드티",
        "셔츠, 블라우스", "오프숄더", "히트텍", "기모제품",
        "가디건", "점퍼", "사파리 자켓", "트위드 자켓",
        "레더 자켓", "숏 코트", "트렌치 코트", "무스탕",
        "경량 패딩", "롱 패딩", "숏 패딩", "바람막이", "후드집업"
    )

    private val selectedpickTop = mutableSetOf<String>() // 선택된 항목 저장
    private lateinit var pickTopBtnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_pick_top)

        val backBtn = findViewById<ImageView>(R.id.backBtn)

        backBtn.setOnClickListener{
            val intent = Intent(this, LoginSignUpConstitutionActivity::class.java)
            startActivity(intent)
            finish()
        }

        val closeBtn = findViewById<ImageView>(R.id.closeBtn)

        closeBtn.setOnClickListener {
            finishAffinity()
        }

        val pickTopSkipText = findViewById<TextView>(R.id.pickTopSkipText)
        val pickTopGrid = findViewById<FlexboxLayout>(R.id.pickTopGrid)
        pickTopBtnNext = findViewById(R.id.pickTopBtnNext)

        pickTopSkipText.setOnClickListener {
            pickTopSkipText.apply {
                setTextColor(ContextCompat.getColor(this@LoginPickTopActivity, R.color.purple))
                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
            val intent = Intent(this, LoginPickBottomsActivity::class.java)
            startActivity(intent)
        }

        pickTopBtnNext.setOnClickListener {
            val intent = Intent(this, LoginPickBottomsActivity::class.java)
            startActivity(intent)
        }

        for (pickTop in pickTopList) {
            val textView = createPickTopItem(pickTop)
            pickTopGrid.addView(textView)
        }
    }

    private fun createPickTopItem(pickTop: String): TextView {
        val textView = TextView(this).apply {
            text = pickTop
            textSize = 18f
            setPadding(80, 20, 80, 20)
            setBackgroundResource(R.drawable.picktop_rounded_edittext_background)
            setTextColor(ContextCompat.getColor(this@LoginPickTopActivity, R.color.black))
            gravity = android.view.Gravity.CENTER
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 25
                bottomMargin = 38
            }
        }

        textView.setOnClickListener {
            pickTopToggleSelection(textView, pickTop)
        }
        return textView
    }

    private fun pickTopToggleSelection(textView: TextView, pickTop: String) {
        if (selectedpickTop.contains(pickTop)) {
            selectedpickTop.remove(pickTop)
            textView.setBackgroundResource(R.drawable.picktop_rounded_edittext_background)
            textView.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            selectedpickTop.add(pickTop)
            textView.setBackgroundResource(R.drawable.picktop_rounded_edittext_selected_background)
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        updatepickTopBtnNextState()
    }

    private fun updatepickTopBtnNextState() {
        if (selectedpickTop.isNotEmpty()) {
            pickTopBtnNext.isEnabled = true
            pickTopBtnNext.backgroundTintList = ContextCompat.getColorStateList(this, R.color.purple)
        } else {
            pickTopBtnNext.isEnabled = false
            pickTopBtnNext.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gray_9E9E9E)
        }
    }
}