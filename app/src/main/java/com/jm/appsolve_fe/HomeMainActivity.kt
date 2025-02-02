package com.jm.appsolve_fe

import android.os.Bundle

import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.jm.appsolve_fe.databinding.ActivityHomeMainBinding

class HomeMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        if (savedInstanceState == null) {
            binding.root.post {
                replaceFragment(Home()) // 첫 화면을 Home으로 설정, UI 로딩 후 실행
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(Home())
                R.id.closet -> replaceFragment(Closet())
                R.id.mypage -> replaceFragment(Mypage())
                else -> {}
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val frameLayout = findViewById<View>(R.id.frame_layout)
        if (frameLayout == null) {
            Log.e("HomeMainActivity", "Error: frame_layout is missing from the layout.")
            return
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null) // 뒤로가기 지원
        fragmentTransaction.commit()
    }
}
