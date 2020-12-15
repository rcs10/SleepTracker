package com.example.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sleeptracker.database.SleepDataBaseDao
import java.lang.IllegalArgumentException

class TrackerViewModelFactory(val application: Application,val sleepDataBaseDao: SleepDataBaseDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TrackerViewModel::class.java))
            return TrackerViewModel(application,sleepDataBaseDao) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}