package com.example.sleeptracker.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sleeptracker.database.OneNight
import com.example.sleeptracker.database.SleepDataBaseDao
import kotlinx.coroutines.*

class QualityViewModel(private val nightId: Long, val sleepDataBaseDao: SleepDataBaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private lateinit var tonight: LiveData<OneNight>
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack


    private suspend fun getTonight(): OneNight? {
        return withContext(Dispatchers.IO) {
            sleepDataBaseDao.getNight(nightId)
        }
    }

    fun ratingsSelected(ratings : Int){
        uiScope.launch {
            val tonight = getTonight() ?: return@launch
            tonight.sleepRating = ratings
            updateDB(tonight)
            _navigateBack.value = true
        }
    }

    private suspend fun updateDB(tonight: OneNight) {
        withContext(Dispatchers.IO){
            sleepDataBaseDao.update(tonight)
        }
    }

    fun resetNavigation(){
        _navigateBack.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}