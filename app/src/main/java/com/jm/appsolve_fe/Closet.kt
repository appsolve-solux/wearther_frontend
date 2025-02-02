package com.jm.appsolve_fe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.jm.appsolve_fe.closet.ClosetPossess
import com.jm.appsolve_fe.closet.ClosetProduct

class Closet : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.closet_main, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val tabPossess = view.findViewById<TextView>(R.id.tab_possess)
            val tabProduct = view.findViewById<TextView>(R.id.tab_product)

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

        private fun replaceFragment(fragment: Fragment) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        private fun setActiveTab(activeTab: TextView, inactiveTab: TextView) {
            context?.let {
                activeTab.setBackgroundColor(ContextCompat.getColor(it, R.color.purple))
                activeTab.setTextColor(ContextCompat.getColor(it, R.color.white))
                inactiveTab.setBackgroundColor(ContextCompat.getColor(it, R.color.gray_EAEBEE))
                inactiveTab.setTextColor(ContextCompat.getColor(it, R.color.gray_757577))
            }
        }

        companion object {
            fun newInstance() = Closet()
        }
}