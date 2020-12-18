package com.example.sleeptracker.sleepnightdetails


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sleeptracker.database.OneNight
import com.example.sleeptracker.database.SleepDataBaseDao
import kotlinx.coroutines.*

class SingleNightViewModel(val nightId: Long, val sleepDataBaseDao: SleepDataBaseDao) :
    ViewModel() {

    val job = Job()
    val uiScope = CoroutineScope(job + Dispatchers.Main)
    private val _oneNight = MutableLiveData<OneNight>()
    val oneNight: LiveData<OneNight>
        get() = _oneNight

    private val _backClicked = MutableLiveData<Boolean>()
    val backClicked: LiveData<Boolean>
        get() = _backClicked

    init {
        getNight()
        _backClicked.value = false
    }

    fun onBackPressed(){
        _backClicked.value = true
    }

    fun resetBackClick(){
        _backClicked.value = false
    }

    private fun getNight() {
        uiScope.launch {
            _oneNight.value = getANight()

        }
    }

    private suspend fun getANight(): OneNight? {
        return withContext(Dispatchers.IO) {
            sleepDataBaseDao.getNight(nightId)
        }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}