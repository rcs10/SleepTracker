package com.example.sleeptracker

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sleeptracker.database.OneNight
import com.example.sleeptracker.database.SleepDataBase
import com.example.sleeptracker.database.SleepDataBaseDao
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class SleepDataBaseTest {
    private lateinit var dataBase: SleepDataBase
    private lateinit var dataBaseDao: SleepDataBaseDao

    @Before
    fun createDB(){
        var context =  InstrumentationRegistry.getInstrumentation().targetContext
        dataBase = Room.inMemoryDatabaseBuilder(context,SleepDataBase::class.java)
            .allowMainThreadQueries()
            .build()

        dataBaseDao = dataBase.sleepDataBaseDao
    }

    @After
    @Throws(Exception::class)
    fun closeDB(){
        dataBase.close()
    }

    @Throws(Exception::class)
    @Test
    fun checkCreationAndInsertion(){
        val night = OneNight()
        dataBaseDao.insert(night)
        val tonight = dataBaseDao.getTonight()
        assertEquals(tonight?.sleepRating, -1)
    }
}