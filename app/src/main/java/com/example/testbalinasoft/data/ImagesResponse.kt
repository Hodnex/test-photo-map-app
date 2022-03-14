package com.example.testbalinasoft.data

import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    val status: Int,
    @SerializedName("data")
    val images: List<Image>
)