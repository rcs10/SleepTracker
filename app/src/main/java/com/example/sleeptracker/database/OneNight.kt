package com.example.sleeptracker.database

import androidx.room.*

@Entity(tableName = "Sleep_Log_Table")
data class OneNight(
    @PrimaryKey(autoGenerate = true)
    var nightId : Long = 0L,
    @ColumnInfo(name = "start_time")
    val startTimeMilli : Long = System.currentTimeMillis(),
    @ColumnInfo(name = "end_time")
    var endTimeMilli : Long = startTimeMilli,
    @ColumnInfo(name = "quality_rating")
    var sleepRating : Int = -1
)
