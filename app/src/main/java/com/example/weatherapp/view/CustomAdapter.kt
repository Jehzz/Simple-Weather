package com.example.weatherapp.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.PokoForecastWeatherData
import com.squareup.picasso.Picasso

class CustomAdapter(val dataSet: PokoForecastWeatherData) :
    RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder =
        CustomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.weather_item_layout,
                    parent,
                    false
                )
        )

    override fun getItemCount(): Int = dataSet.list.size


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.onBind(dataSet, position)
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tvTime: TextView = itemView.findViewById(R.id.tv_time)
        var tvTemp: TextView = itemView.findViewById(R.id.tv_temp)
        var ivWeatherIcon: ImageView = itemView.findViewById(R.id.iv_weather_icon)

        fun onBind(data: PokoForecastWeatherData, position: Int) {
            //pass data to weather_item_layout views
            tvTime.text = (data.list[position].dt_txt).substring(11, 16)
            tvTemp.text = data.list[position].main.temp.substring(0, 2) + "°"
            var iconString = data.list[position].weather[0].icon
            //todo: sorting function to find max and min temps to assign correct icon
            Picasso.get().load("http://openweathermap.org/img/wn/$iconString@2x.png")
                .into(ivWeatherIcon)
        }
    }
}