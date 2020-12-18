package com.example.sleeptracker.database

import android.content.Context
import androidx.room.*

@Database(entities = [OneNight::class],version = 1)
abstract class SleepDataBase : RoomDatabase() {
    abstract val sleepDataBaseDao : SleepDataBaseDao

    companion object{
        @Volatile
        private var INSTANCE : SleepDataBase? = null

        fun getInstance(context : Context) : SleepDataBase {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context,SleepDataBase::class.java,"sleep_history_db")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}