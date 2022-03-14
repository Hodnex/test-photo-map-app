package com.example.testbalinasoft.data

import com.google.gson.annotations.SerializedName

data class CommentsResponse(
    val status: Int,
    @SerializedName("data")
    val comments: List<Comment>
)