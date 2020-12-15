package com.example.sleeptracker.sleepquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.sleeptracker.database.SleepDataBaseDao

class QualityViewModelFactory(private val nightId : Long , val sleepDataBaseDao: SleepDataBaseDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(QualityViewModel::class.java))
            return QualityViewModel(nightId,sleepDataBaseDao) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}