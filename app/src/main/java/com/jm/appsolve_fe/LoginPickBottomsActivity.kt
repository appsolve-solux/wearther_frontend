package com.jm.appsolve_fe

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout

class LoginPickBottomsActivity : AppCompatActivity() {

    private val pickBottomList = listOf(
        "숏 팬츠", "슬랙스", "데님", "와이드 팬츠",
        "스키니", "부츠컷", "조거 팬츠", "점프 수트",
        "반바지", "기모바지", "미니 스커트", "미디 스커트",
        "롱 스커트"
    )

    private val selectedPickBottomIndexes = mutableSetOf<Int>() // 선택된 항목 저장
    private lateinit var pickBottomsBtnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_pick_bottoms)

        val backBtn = findViewById<ImageView>(R.id.backBtn)

        backBtn.setOnClickListener{
            val intent = Intent(this, LoginPickTopActivity::class.java)
            startActivity(intent)
            finish()
        }

        val closeBtn = findViewById<ImageView>(R.id.closeBtn)

        closeBtn.setOnClickListener {
            finishAffinity()
        }

        val pickBottomsSkipText = findViewById<TextView>(R.id.pickBottomsSkipText)
        val pickBottomsGrid = findViewById<FlexboxLayout>(R.id.pickBottomsGrid)
        pickBottomsBtnNext = findViewById(R.id.pickBottomsBtnNext)

        pickBottomsSkipText.setOnClickListener {
            pickBottomsSkipText.apply {
                setTextColor(ContextCompat.getColor(this@LoginPickBottomsActivity, R.color.purple))
                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
            SignUpDataStore.saveData(this@LoginPickBottomsActivity, "lowers", "")

            val intent = Intent(this, LoginPickEctActivity::class.java)
            startActivity(intent)
        }

        pickBottomsBtnNext.setOnClickListener {
            SignUpDataStore.saveData(this, "lowers", selectedPickBottomIndexes.joinToString(","))
            val intent = Intent(this, LoginPickEctActivity::class.java)
            startActivity(intent)
        }

        for ((index, pickBottom) in pickBottomList.withIndex()) {
            val textView = createPickTopItem(pickBottom, index)
            pickBottomsGrid.addView(textView)
        }
    }

    private fun createPickTopItem(pickBottom: String, index: Int): TextView {
        val textView = TextView(this).apply {
            text = pickBottom
            textSize = 18f
            setPadding(80, 20, 80, 20)
            setBackgroundResource(R.drawable.picktop_rounded_edittext_background)
            setTextColor(ContextCompat.getColor(this@LoginPickBottomsActivity, R.color.black))
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
            pickTopToggleSelection(textView, index)
        }
        return textView
    }

    private fun pickTopToggleSelection(textView: TextView, index: Int) {
        if (selectedPickBottomIndexes.contains(index)) {
            selectedPickBottomIndexes.remove(index)
            textView.setBackgroundResource(R.drawable.picktop_rounded_edittext_background)
            textView.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            selectedPickBottomIndexes.add(index)
            textView.setBackgroundResource(R.drawable.picktop_rounded_edittext_selected_background)
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        updatepickTopBtnNextState()
    }

    private fun updatepickTopBtnNextState() {
        if (selectedPickBottomIndexes.isNotEmpty()) {
            pickBottomsBtnNext.isEnabled = true
            pickBottomsBtnNext.backgroundTintList = ContextCompat.getColorStateList(this, R.color.purple)
        } else {
            pickBottomsBtnNext.isEnabled = false
            pickBottomsBtnNext.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gray_9E9E9E)
        }
    }
}