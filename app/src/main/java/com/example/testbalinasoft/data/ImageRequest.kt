package com.example.testbalinasoft.data

data class ImageRequest(
    val base64Image: String,
    val date: Int,
    val lat: Double,
    val lng: Double
)