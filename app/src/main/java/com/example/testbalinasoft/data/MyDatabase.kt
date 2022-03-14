package com.example.testbalinasoft.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Image::class, Comment::class, User::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun roomDao(): MyDao
}