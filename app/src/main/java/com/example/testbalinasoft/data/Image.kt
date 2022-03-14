package com.example.testbalinasoft.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "image_table")
@Parcelize
data class Image(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val url: String?,
    val date: Int,
    val lat: Double,
    val lng: Double
) : Parcelable