package com.jm.appsolve_fe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookmarkAdapter(var bList: List<BookmarkData>) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    inner class BookmarkViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val firstaddress : TextView = itemView.findViewById(R.id.tv_1st_address)
        val secondaddress : TextView = itemView.findViewById(R.id.tv_2nd_address)
        val thirdaddress : TextView = itemView.findViewById(R.id.tv_3rd_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_location_item_layout,parent,false)
        return BookmarkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bList.size
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.firstaddress.text = bList[position].firstaddress
        holder.secondaddress.text = bList[position].secondaddress
        holder.thirdaddress.text = bList[position].thirdaddress
    }
}