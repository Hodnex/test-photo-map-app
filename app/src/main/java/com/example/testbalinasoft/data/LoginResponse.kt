package com.example.testbalinasoft.data

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    val status: Int,
    @SerializedName("data")
    val user: User
)