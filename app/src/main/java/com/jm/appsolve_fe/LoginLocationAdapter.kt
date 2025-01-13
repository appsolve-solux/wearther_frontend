package com.jm.appsolve_fe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class LoginLocationAdapter (private var locationList : List<String>) :
    RecyclerView.Adapter<LoginLocationAdapter.LocationViewHolder>() {

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locationText: TextView = itemView.findViewById(R.id.locationTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.login_item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.locationText.text = locationList[position]
    }

    override fun getItemCount(): Int = locationList.size

    fun updateList(newList: List<String>) {
        locationList = newList
        notifyDataSetChanged()
    }
}


