package com.jm.appsolve_fe

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jm.appsolve_fe.databinding.FragmentLocationBinding
import com.jm.appsolve_fe.databinding.SearchLocationDialogBinding

class Location : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    // 즐겨찾기
    private lateinit var bookmarkrecyclerView: RecyclerView
    private var bList = ArrayList<BookmarkData>()
    private lateinit var bookmarkadapter: BookmarkAdapter

    // dialog 내부
    private lateinit var loclistrecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<LocationData>()
    private lateinit var locationlistadapter: LocationAdapter

    private lateinit var justtext: TextView

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val view = binding.root

        //툴바 타이틀 변경
        val toolbarTitle = view.findViewById<TextView>(R.id.tv_toolbar_title)
        toolbarTitle.text = "위치"

        // 툴바 >버튼 클릭 이벤트
        val backBtn: AppCompatImageView = view.findViewById(R.id.backbtn)
        backBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(Home())
        }

        val currentLocationLayout: LinearLayout = view.findViewById(R.id.currentlocationlayout)

        bookmarkrecyclerView = view.findViewById(R.id.bookmark_location_recyclerView)
        bookmarkrecyclerView.setHasFixedSize(true)
        bookmarkrecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        bookmarkadapter = BookmarkAdapter(bList) { position ->
            showDeleteDialog(position)
        }
        bookmarkrecyclerView.adapter = bookmarkadapter

        // 위치 조정 화살표 visibility
        val upArrow: ImageView = view.findViewById(R.id.locationup)
        val downArrow: ImageView = view.findViewById(R.id.locationdown)

        if (currentLocationLayout.visibility == View.VISIBLE) {
            upArrow.visibility = View.GONE
            downArrow.visibility = View.GONE
        } else {
            upArrow.visibility = View.VISIBLE
            downArrow.visibility = View.VISIBLE
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // dialog.show
        val selectItemButton: ImageButton = view.findViewById(R.id.opendialog)
        selectItemButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {

        val dialogBinding = SearchLocationDialogBinding.inflate(layoutInflater)

        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogBinding.root)

        loclistrecyclerView = dialogBinding.recyclerView
        searchView = dialogBinding.searchView

        // 위치 목록 추가
        addLocToList()

        loclistrecyclerView.setHasFixedSize(true)
        loclistrecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        // 리사이클러뷰 추가
        locationlistadapter = LocationAdapter(mList)
        loclistrecyclerView.adapter = locationlistadapter


        locationlistadapter.itemClickListener = object : LocationAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = mList[position]
                Toast.makeText(context, "${item.address} 클릭", Toast.LENGTH_SHORT).show()

                // 텍스트 파싱
                val addressParts = item.address.split(" ")

                val newBookmark = BookmarkData(addressParts[0], addressParts.getOrElse(1) { "" }, addressParts.getOrElse(2) { "" })
                bList.add(newBookmark)
                bookmarkadapter.notifyDataSetChanged()

                dialog.dismiss()
            }
        }

        // searchview filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.TOP)
        window?.setDimAmount(0f)

        dialog.show()
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<LocationData>()
            for (i in mList) {
                if(i.address.contains(query.trim())){
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()){
                Toast.makeText(context, "No Data found", Toast.LENGTH_LONG).show()
            } else {
                locationlistadapter.setFilteredList(filteredList)
            }
        }
    }


    private fun addLocToList() {
        mList.add(LocationData("서울특별시 용산구 청파동"))
        mList.add(LocationData("서울특별시 용산구 뭐뭐동"))
        mList.add(LocationData("서울특별시 송파구 일이동"))
        mList.add(LocationData("서울특별시 마포구 일이동"))
        mList.add(LocationData("서울특별시 마포구 성암동"))
        mList.add(LocationData("서울특별시 용산구 청파동1"))
        mList.add(LocationData("서울특별시 용산구 뭐뭐동2"))
        mList.add(LocationData("서울특별시 송파구 일이동3"))
        mList.add(LocationData("서울특별시 마포구 일이동4"))
        mList.add(LocationData("서울특별시 마포구 성암동5"))

    }

    private fun showDeleteDialog(position: Int) {
        // Custom Dialog View
        val dialogView = LayoutInflater.from(context).inflate(R.layout.bookmark_delete_custom_dialog, null)
        val dialog = Dialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)

        // 선택된 아이템의 이름 가져오기
        val selectedBookmarkName = "${bList[position].secondaddress} ${bList[position].thirdaddress}" // BookmarkData의 필드 중 name 사용

        // Dialog의 alert_text에 선택된 이름 설정
        val alertTextView = dialogView.findViewById<TextView>(R.id.alert_text)
        alertTextView.text = selectedBookmarkName // alert_text는 커스텀 Dialog 레이아웃의 ID입니다.

        // Dialog "확인" 버튼
        val confirmButton = dialogView.findViewById<TextView>(R.id.delete)
        confirmButton.setOnClickListener {

            bList.removeAt(position) // 선택된 아이템 삭제
            bookmarkadapter.notifyDataSetChanged() // RecyclerView 갱신
            dialog.dismiss()
        }

        // Dialog "취소" 버튼
        val cancelButton = dialogView.findViewById<TextView>(R.id.cancel_delete)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        val window = dialog.window
        window?.setWindowAnimations(0) // 애니메이션 제거

        dialog.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
