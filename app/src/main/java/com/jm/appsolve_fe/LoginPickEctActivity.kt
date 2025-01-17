package com.jm.appsolve_fe

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout

class LoginPickEctActivity : AppCompatActivity() {

    private val pickEctList = listOf(
        "힙", "캐주얼", "페미닌", "모던", "드뮤어", "고프코어", "Y2K", "올드머니"
    )
    private val styleViewMap = mutableMapOf<String, Pair<Int, Int>>() // <항목 이름, Pair(숫자 원 ID, 레이아웃 ID)>

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

    private fun createPickTopItem(pickTop: String): ConstraintLayout {
        val layout = ConstraintLayout(this).apply {
            layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 20
                bottomMargin = 20
            }
            id = View.generateViewId()
            clipChildren = false
            clipToPadding = false
        }

        val textView = TextView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            }
            text = pickTop
            setPadding(80, 20, 80, 20)
            textSize = 18f
            setBackgroundResource(R.drawable.picktop_rounded_edittext_background)
            setTextColor(ContextCompat.getColor(this@LoginPickEctActivity, R.color.black))
            gravity = Gravity.CENTER
            id = View.generateViewId()
        }

        val numberCircle = TextView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                80, 80
            ).apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                marginStart = -20
                topMargin = -20
            }
            textSize = 16f
            gravity = Gravity.CENTER
            background = ContextCompat.getDrawable(this@LoginPickEctActivity, R.drawable.circle_background)
            visibility = View.INVISIBLE
            setTextColor(ContextCompat.getColor(this@LoginPickEctActivity, R.color.purple))
            id = View.generateViewId()
        }


        layout.addView(textView)
        layout.addView(numberCircle)

        styleViewMap[pickTop] = Pair(numberCircle.id, layout.id)

        layout.setOnClickListener {
            pickTopToggleSelection(layout, textView, numberCircle, pickTop)
        }

        return layout
    }

    private fun pickTopToggleSelection(
        layout: ConstraintLayout,
        textView: TextView,
        numberCircle: TextView,
        pickTop: String
    ) {
        if (selectedpickEct.contains(pickTop)) {
            selectedpickEct.remove(pickTop)
            textView.setBackgroundResource(R.drawable.picktop_rounded_edittext_background)
            textView.setTextColor(ContextCompat.getColor(this, R.color.black))
            numberCircle.visibility = View.INVISIBLE
            updateStyleNumbers()
        } else if (selectedpickEct.size < 3){
            selectedpickEct.add(pickTop)
            textView.setBackgroundResource(R.drawable.picktop_rounded_edittext_selected_background)
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))
            numberCircle.visibility = View.VISIBLE
            updateStyleNumbers()
        }
        updatepickTopBtnNextState()
    }

    private fun updateStyleNumbers() {
        for (i in selectedpickEct.indices) {
            val style = selectedpickEct.elementAt(i)
            val (numberCircleId, _) = styleViewMap[style] ?: continue
            val numberCircle = findViewById<TextView>(numberCircleId)
            numberCircle.text = (i + 1).toString()
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