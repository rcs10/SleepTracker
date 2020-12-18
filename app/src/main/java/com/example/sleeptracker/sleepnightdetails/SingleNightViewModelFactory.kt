package com.example.sleeptracker.sleepnightdetails

import android.view.autofill.AutofillId
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sleeptracker.database.SleepDataBaseDao
import java.lang.IllegalArgumentException
import kotlin.jvm.Throws

class SingleNightViewModelFactory(val nightId: Long, val sleepDataBaseDao: SleepDataBaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SingleNightViewModel::class.java))
            return SingleNightViewModel(nightId, sleepDataBaseDao) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}