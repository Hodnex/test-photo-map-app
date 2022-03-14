package com.example.testbalinasoft.data

import com.google.gson.annotations.SerializedName

data class ImageResponse (
    val status: Int,
    @SerializedName("data")
    val image: Image
    )