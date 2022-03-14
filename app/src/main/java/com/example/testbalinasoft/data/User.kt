package com.example.testbalinasoft.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = false)
    val userId: Int,
    val login: String,
    val token: String
    )