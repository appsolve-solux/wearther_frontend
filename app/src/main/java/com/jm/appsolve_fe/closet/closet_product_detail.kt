package com.jm.appsolve_fe.closet

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.jm.appsolve_fe.R


class closet_product_detail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.closet_product_detail)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 각 탭에 사용할 Fragment 초기화
        val tab01: Fragment = tab01()
        val tab02: Fragment = tab02()

        supportFragmentManager.beginTransaction().replace(R.id.container, tab01).commit()

        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.addTab(tabs.newTab().setText("힙"))
        tabs.addTab(tabs.newTab().setText("캐주얼"))

        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position

                val selectedFragment = when (position) {
                    0 -> tab01
                    1 -> tab02
                    else -> null
                }
                selectedFragment?.let {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, it)
                        .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Tab이 선택 해제될 때 필요한 작업을 여기에 작성
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Tab이 다시 선택될 때 필요한 작업을 여기에 작성
            }
        })
    }
}