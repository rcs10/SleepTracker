package com.example.sleeptracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDataBaseDao {
    @Insert
    fun insert(oneNight: OneNight)

    @Update
    fun update(oneNight: OneNight)

    @Query("select * from sleep_log_table where nightId = :id")
    fun getNight(id : Long) : OneNight?

    @Query("delete from sleep_log_table")
    fun deleteAll()

    @Query("select * from sleep_log_table order by nightId desc")
    fun getAllNight() : LiveData<List<OneNight>>

    @Query("select * from sleep_log_table order by nightId desc limit 1")
    fun getTonight() : OneNight?
}