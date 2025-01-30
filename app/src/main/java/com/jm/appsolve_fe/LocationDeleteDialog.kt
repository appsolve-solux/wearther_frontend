package com.jm.appsolve_fe

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast

class LocationDeleteDialog(
    private val context: Context,
    private val position: Int,
    private val bookmarkList: ArrayList<BookmarkData>,
    private val bookmarkAdapter: BookmarkAdapter
) {

    fun show() {
        // Custom Dialog View
        val dialogView = LayoutInflater.from(context).inflate(R.layout.bookmark_delete_custom_dialog, null)
        val dialog = Dialog(context, R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)

        // 선택된 아이템의 이름 가져오기
        val selectedBookmarkName = "${bookmarkList[position].secondaddress} ${bookmarkList[position].thirdaddress}"

        // Dialog의 alert_text에 선택된 이름 설정
        val alertTextView = dialogView.findViewById<TextView>(R.id.alert_text)
        alertTextView.text = selectedBookmarkName

        // Dialog "확인" 버튼
        val confirmButton = dialogView.findViewById<TextView>(R.id.delete)
        confirmButton.setOnClickListener {
            // Custom toast message
            val toastMessage = "${bookmarkList[position].secondaddress} ${bookmarkList[position].thirdaddress}이(가) 즐겨찾기에서 삭제되었습니다."
            val toast = LayoutInflater.from(context).inflate(R.layout.location_toast_layout, null)
            val tvToast = toast.findViewById<TextView>(R.id.tvToast)
            tvToast.text = toastMessage

            Toast(context).apply {
                duration = Toast.LENGTH_SHORT
                setGravity(Gravity.BOTTOM, 0, 250)
                view = toast
            }.show()

            // 아이템 삭제
            bookmarkList.removeAt(position)
            bookmarkAdapter.notifyDataSetChanged() // RecyclerView 갱신

            dialog.dismiss()
        }

        // Dialog "취소" 버튼
        val cancelButton = dialogView.findViewById<TextView>(R.id.cancel_delete)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // 윈도우 애니메이션 제거
        val window = dialog.window
        window?.setWindowAnimations(0)

        dialog.show()
    }
}
