package com.example.praktikdrhfanina.model

import com.google.gson.annotations.SerializedName

data class CreatePatientRequest(
    @SerializedName("name")
    val username: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    val email: String,
    val password: String,
)
