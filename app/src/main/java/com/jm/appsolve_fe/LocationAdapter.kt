package com.jm.appsolve_fe

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocationAdapter(var mList: List<LocationData>) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

        inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val locationTv : TextView = itemView.findViewById(R.id.tv_addresslist)
        }

        fun setFilteredList(mList: List<LocationData>) {
            this.mList = mList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout,parent,false)
            return LocationViewHolder(view)
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
            holder.locationTv.text = mList[position].address
        }
}