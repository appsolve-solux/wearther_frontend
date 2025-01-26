package com.jm.appsolve_fe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LoginLocationBottomSheetActivity : BottomSheetDialogFragment() {

    private lateinit var locationAdapter: LoginLocationAdapter
    private var onLocationSelected: ((String) -> Unit)? = null

    fun setOnLocationSelectedListener(listener: (String) -> Unit) {
        onLocationSelected = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.login_bottom_sheet_location, container, false)

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val recyclerView = view.findViewById<RecyclerView>(R.id.locationRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        locationAdapter = LoginLocationAdapter(LocationData.locationList) { selectedLocation ->
            onLocationSelected?.invoke(selectedLocation)
            dismiss()
        }
        recyclerView.adapter = locationAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                val filteredList = LocationData.locationList.filter {
                    it.contains(query, ignoreCase = true)
                }
                locationAdapter.updateList(filteredList)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // DrawableEnd의 인덱스
                val drawableWidth = searchEditText.compoundDrawables[drawableEnd]?.bounds?.width() ?: 0

                if (event.rawX >= (searchEditText.right - drawableWidth)) {
                    // 텍스트 지우기
                    searchEditText.text.clear()
                    true // 이벤트를 처리했음을 반환
                } else {
                    false // 이벤트를 처리하지 않았음을 반환
                }
            } else {
                false // 다른 이벤트는 처리하지 않음
            }
        }

        return view
    }
}