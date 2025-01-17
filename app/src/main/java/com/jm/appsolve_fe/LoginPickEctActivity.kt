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

class LoginPickEctActivity : AppCompatActivity() {

    private val pickEctList = listOf(
        "힙", "캐주얼", "페미닌", "모던", "드뮤어", "고프코어", "Y2K", "올드머니"
    )

    private val selectedpickEct = mutableSetOf<String>() // 선택된 항목 저장
    private lateinit var pickEctBtnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_pick_ect)

        val backBtn = findViewById<ImageView>(R.id.backBtn)

        backBtn.setOnClickListener{
            val intent = Intent(this, LoginPickBottomsActivity::class.java)
            startActivity(intent)
            finish()
        }

        val closeBtn = findViewById<ImageView>(R.id.closeBtn)

        closeBtn.setOnClickListener {
            finishAffinity()
        }

        val pickEctSkipText = findViewById<TextView>(R.id.pickEctSkipText)
        val pickEctGrid = findViewById<FlexboxLayout>(R.id.pickEctGrid)
        pickEctBtnNext = findViewById(R.id.pickEctBtnNext)

        pickEctSkipText.setOnClickListener {
            pickEctSkipText.apply {
                setTextColor(ContextCompat.getColor(this@LoginPickEctActivity, R.color.purple))
                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }

        pickEctBtnNext.setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }

        for (pickTop in pickEctList) {
            val textView = createPickTopItem(pickTop)
            pickEctGrid.addView(textView)
        }
    }

    private fun createPickTopItem(pickTop: String): TextView {
        val textView = TextView(this).apply {
            text = pickTop
            textSize = 18f
            setPadding(80, 20, 80, 20)
            setBackgroundResource(R.drawable.picktop_rounded_edittext_background)
            setTextColor(ContextCompat.getColor(this@LoginPickEctActivity, R.color.black))
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

    private fun pickTopToggleSelection(textView: TextView, pickBottom: String) {
        if (selectedpickEct.contains(pickBottom)) {
            selectedpickEct.remove(pickBottom)
            textView.setBackgroundResource(R.drawable.picktop_rounded_edittext_background)
            textView.setTextColor(ContextCompat.getColor(this, R.color.black))
            updateStyleNumbers()
        } else if (selectedpickEct.size < 3){
            selectedpickEct.add(pickBottom)
            textView.setBackgroundResource(R.drawable.picktop_rounded_edittext_selected_background)
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))
            updateStyleNumbers()
        }
        updatepickTopBtnNextState()
    }

    private fun updateStyleNumbers() {
        val pickEctGrid = findViewById<FlexboxLayout>(R.id.pickEctGrid)
        for (i in 0 until pickEctGrid.childCount) {
            val child = pickEctGrid.getChildAt(i) as TextView
            val style = child.text.toString().replace(Regex("^\\d\\s"), "")
            if (selectedpickEct.contains(style)) {
                val index = selectedpickEct.indexOf(style) + 1
                child.text = "$index $style"
            } else {
                child.text = style
            }
        }
    }

    private fun updatepickTopBtnNextState() {
        pickEctBtnNext.isEnabled = selectedpickEct.size == 3
        pickEctBtnNext.backgroundTintList = if (selectedpickEct.size == 3) {
            ContextCompat.getColorStateList(this, R.color.purple)
        } else {
            ContextCompat.getColorStateList(this, R.color.gray_9E9E9E)
        }
    }
}