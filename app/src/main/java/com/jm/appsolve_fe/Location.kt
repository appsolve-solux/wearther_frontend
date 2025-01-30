package com.jm.appsolve_fe

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jm.appsolve_fe.databinding.FragmentLocationBinding
import com.jm.appsolve_fe.databinding.SearchLocationDialogBinding
import java.util.Collections

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
            (activity as HomeMainActivity).replaceFragment(Home())
        }

        val currentLocationLayout: LinearLayout = view.findViewById(R.id.currentlocationlayout)

        bookmarkrecyclerView = view.findViewById(R.id.bookmark_location_recyclerView)
        bookmarkrecyclerView.setHasFixedSize(true)
        bookmarkrecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        bookmarkadapter = BookmarkAdapter(bList) { position ->
            LocationDeleteDialog(requireContext(), position, bList, bookmarkadapter).show()
        }
        bookmarkrecyclerView.adapter = bookmarkadapter

        // 즐겨찾기 리사이클러뷰 drag
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,0) {
            override fun onMove(
                recyclerView: RecyclerView,
                source: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = source.adapterPosition
                val targetPosition = target.adapterPosition

                Collections.swap(bList,sourcePosition,targetPosition)
                bookmarkadapter.notifyItemMoved(sourcePosition,targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

        })
        itemTouchHelper.attachToRecyclerView(bookmarkrecyclerView)

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
            // BottomSheetDialog 호출
            LocationBottomSheetDialog { selectedLocation ->
                val addressParts = selectedLocation.address.split(" ")
                //텍스트 파싱
                val newBookmark = BookmarkData(
                    addressParts.getOrElse(1) { "" },
                    addressParts.getOrElse(2) { "" },
                    addressParts.getOrElse(3) { "" }
                )
                bList.add(newBookmark)
                bookmarkadapter.notifyDataSetChanged()
            }.show(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
