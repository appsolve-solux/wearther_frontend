package com.jm.appsolve_fe.closet

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jm.appsolve_fe.R

class ClosetMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.closet_main)

        // Fragment 전환 함수
        fun replaceFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        // 탭 활성화 상태 변경
        fun setActiveTab(activeTab: TextView, inactiveTab: TextView) {
            activeTab.setBackgroundColor(ContextCompat.getColor(this, R.color.purple)) // 활성화된 탭 배경색
            activeTab.setTextColor(ContextCompat.getColor(this, R.color.white)) // 활성화된 탭 글자색
            inactiveTab.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_EAEBEE)) // 비활성화된 탭 배경색
            inactiveTab.setTextColor(ContextCompat.getColor(this, R.color.gray_757577)) // 비활성화된 탭 글자색
        }
        val tabPossess = findViewById<TextView>(R.id.tab_possess)
        val tabProduct = findViewById<TextView>(R.id.tab_product)

        // 기본 화면 설정
        replaceFragment(ClosetPossess())
        setActiveTab(tabPossess, tabProduct)

        // "보유한 옷" 탭 클릭 이벤트
        tabPossess.setOnClickListener {
            replaceFragment(ClosetPossess())
            setActiveTab(tabPossess, tabProduct)
        }

        // "추천 상품" 탭 클릭 이벤트
        tabProduct.setOnClickListener {
            replaceFragment(ClosetProduct())
            setActiveTab(tabProduct, tabPossess)
        }
    }
}