package com.example.testbalinasoft.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "comment_table")
@Parcelize
data class Comment(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val date: Long,
    val text: String,
    val imageId: Int
) : Parcelable