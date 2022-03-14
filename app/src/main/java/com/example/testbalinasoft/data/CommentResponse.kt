package com.example.testbalinasoft.data

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    val status: Int,
    @SerializedName("data")
    val comment: Comment
)