package com.example.sleeptracker.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.sleeptracker.R
import com.example.sleeptracker.convertLongToDateString
import com.example.sleeptracker.convertNumericQualityToString
import com.example.sleeptracker.database.OneNight

@BindingAdapter("qualityText")
fun TextView.setQualityText(oneNight: OneNight?){
    oneNight?.let{
        text = convertNumericQualityToString(oneNight.sleepRating, context.resources)
    }
}

@BindingAdapter("qualityImage")
fun ImageView.setQualityImage(oneNight: OneNight?){
    oneNight?.let {
        setImageResource(when (oneNight.sleepRating) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_waiting
        })
    }
}

@BindingAdapter("startTime")
fun TextView.setStartTime(oneNight: OneNight?){
    oneNight?.apply {
        text = convertLongToDateString(oneNight.startTimeMilli)
    }
}

@BindingAdapter("endTime")
fun TextView.setEndTime(oneNight: OneNight?){
    oneNight?.apply {
        text = convertLongToDateString(oneNight.endTimeMilli)
    }
}