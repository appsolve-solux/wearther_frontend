package com.jm.appsolve_fe

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.jm.appsolve_fe.retrofit.DeleteBookmarkLocationRequest
import com.jm.appsolve_fe.retrofit.LWRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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


            val locationIndex = position+1
            val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNCIsImlhdCI6MTczODMxMDM4OCwiZXhwIjoxNzQwOTAyMzg4fQ.Pma0mIzOiSPvUto6Ya8qs1Cncoz5W2QBkOiZiGkiD40" // 실제 토큰을 여기에 넣어야 함

            LWRetrofitClient.service.deleteBookmarkLocation("Bearer $token", locationIndex)
                .enqueue(object : Callback<DeleteBookmarkLocationRequest> {
                    override fun onResponse(
                        call: Call<DeleteBookmarkLocationRequest>,
                        response: Response<DeleteBookmarkLocationRequest>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("Location", "API Response Successful: ${response.body()}")
                            // 서버에서 삭제 후 UI에서 아이템 삭제
                            bookmarkList.removeAt(position)
                            bookmarkAdapter.notifyDataSetChanged()

                            // 삭제 완료 메시지 Toast
                            Toast(context).apply {
                                duration = Toast.LENGTH_SHORT
                                setGravity(Gravity.BOTTOM, 0, 250)
                                view = toast
                            }.show()

                            dialog.dismiss()
                        } else {
                            Log.e("Location", "API Response Error: ${response.code()} - ${response.message()}")
                            Toast.makeText(context, "삭제 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DeleteBookmarkLocationRequest>, t: Throwable) {
                        Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

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