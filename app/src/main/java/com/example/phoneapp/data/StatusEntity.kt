package com.example.phoneapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statuses")
data class StatusEntity(
    @PrimaryKey val id: Int,
    val name: String
)
