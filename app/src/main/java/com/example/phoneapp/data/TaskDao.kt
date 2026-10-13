package com.example.phoneapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE statusId != :completedStatus ORDER BY startTimeMillis ASC")
    suspend fun getAllNotCompleted(completedStatus: Int = Status.COMPLETED): List<TaskEntity>
}
