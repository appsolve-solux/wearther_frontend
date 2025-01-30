package com.jm.appsolve_fe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeWeatherAdapter(var wList: List<HomeWeatherData>) : RecyclerView.Adapter<HomeWeatherAdapter.HomeWeatherViewHolder>() {

    inner class HomeWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time : TextView = itemView.findViewById(R.id.home_weather_time)
        val weatherimg : ImageView = itemView.findViewById(R.id.home_weather_img)
        val temperature : TextView = itemView.findViewById(R.id.home_weather_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeWeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_weather_item,parent,false)
        val lp = view.layoutParams
        //
        lp.width = parent.measuredWidth / 7

        return HomeWeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wList.size
    }

    override fun onBindViewHolder(holder: HomeWeatherViewHolder, position: Int) {
        holder.time.text = wList[position].time
        holder.weatherimg.setImageResource(wList[position].weatherimg)
        holder.temperature.text = wList[position].temperature

    }

}