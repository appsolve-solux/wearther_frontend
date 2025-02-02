package com.jm.appsolve_fe

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class Mypage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        // '프로필 편집' 버튼 클릭 시 ProfileEditActivity로 이동
        val editProfileButton = view.findViewById<Button>(R.id.btnEditProfile)
        editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), ProfileEditActivity::class.java)
            startActivity(intent)
        }

        // '로그아웃' 텍스트 클릭 시 로그아웃 다이얼로그 띄우기
        val logoutTextView = view.findViewById<TextView>(R.id.tvLogout)
        logoutTextView.setOnClickListener {
            showLogoutDialog()
        }

        // '계정 삭제' 텍스트 클릭 시 계정 삭제 다이얼로그 띄우기
        val deleteAccountTextView = view.findViewById<TextView>(R.id.tvDeleteAccount)
        deleteAccountTextView.setOnClickListener {
            showDeleteAccountDialog()
        }

        return view
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("로그아웃 하시겠습니까?")
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("네") { _, _ ->
                performLogout()
            }
            .show()
    }

    private fun performLogout() {
        // 로그아웃 처리 후 로그인 화면으로 이동
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("계정을 삭제하시겠습니까?")
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("네") { _, _ ->
                performDeleteAccount()
            }
            .show()
    }

    private fun performDeleteAccount() {
        // 실제 계정 삭제 로직을 수행하는 부분

        // 계정 삭제 후 로그인 화면으로 이동
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}