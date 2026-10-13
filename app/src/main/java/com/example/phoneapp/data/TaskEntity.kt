package com.example.phoneapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = StatusEntity::class,
            parentColumns = ["id"],
            childColumns = ["statusId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("statusId")]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shortName: String,
    val description: String,
    val startTimeMillis: Long,
    val durationHours: Int,
    val difficulty: Int,
    val location: String?,
    val createdDayKey: Int,
    val statusId: Int
)
