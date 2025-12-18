package com.example.praktikdrhfanina.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    val user: User
)
