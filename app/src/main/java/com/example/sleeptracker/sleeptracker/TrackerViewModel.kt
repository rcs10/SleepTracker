package com.example.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sleeptracker.database.OneNight
import com.example.sleeptracker.database.SleepDataBaseDao
import com.example.sleeptracker.formatNights
import kotlinx.coroutines.*

class TrackerViewModel(application: Application, private val sleepDataBaseDao: SleepDataBaseDao) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val tonight = MutableLiveData<OneNight?>()
    var allNights = sleepDataBaseDao.getAllNight()
    var allNightsString = Transformations.map(allNights) {
        formatNights(it, application.resources)
    }
    private val _navigateToQuality = MutableLiveData<OneNight?>()
    val navigateToQuality: LiveData<OneNight?>
        get() = _navigateToQuality

    private val _showSnackBar = MutableLiveData<Boolean>()
    val showSnackbar: LiveData<Boolean>
        get() = _showSnackBar

    private val _navigateToDisplayScreen = MutableLiveData<Long>()
    val navigateToDisplayScreen : LiveData<Long>
        get() = _navigateToDisplayScreen

    fun resetSnackBarStatus(){
        _showSnackBar.value = false
    }

    init {
        initializeNights()
        //getAllNights()
    }

    val startButtonStatus = Transformations.map(tonight) {
        it == null
    }

    val stopButtonStatus = Transformations.map(tonight) {
        it != null
    }

    val clearButtonStatus = Transformations.map(allNights) {
        it.isNotEmpty()
    }

    private fun getAllNights() {
        uiScope.launch {
            allNights = getAllNightsFromDB()
        }
    }

    private suspend fun getAllNightsFromDB(): LiveData<List<OneNight>> {
        return withContext(Dispatchers.IO) {
            sleepDataBaseDao.getAllNight()
        }
    }

    private fun initializeNights() {
        uiScope.launch {
            tonight.value = getTonight()
        }
    }

    private suspend fun getTonight(): OneNight? {
        return withContext(Dispatchers.IO) {
            var nyt = sleepDataBaseDao.getTonight()
            if (nyt?.endTimeMilli != nyt?.startTimeMilli)
                nyt = null

            nyt
        }
    }

    fun onStart() {
        uiScope.launch {
            val night = OneNight()
            insertIntoDB(night)
            tonight.value = getTonight()
        }
    }

    private suspend fun insertIntoDB(night: OneNight) {
        withContext(Dispatchers.IO) {
            sleepDataBaseDao.insert(night)
        }
    }

    fun onStop() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            updateTonight(oldNight)
            _navigateToQuality.value = oldNight
        }
    }

    private suspend fun updateTonight(tonight: OneNight) {
        withContext(Dispatchers.IO) {
            sleepDataBaseDao.update(tonight)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            _showSnackBar.value = true
            tonight.value = null
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            sleepDataBaseDao.deleteAll()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun resetNavigation() {
        _navigateToQuality.value = null
    }

    fun goToDisplayScreen(nightId: Long) {
        _navigateToDisplayScreen.value = nightId
    }

    fun resetNavigationToDisplayScreen(){
        _navigateToDisplayScreen.value = null
    }
}