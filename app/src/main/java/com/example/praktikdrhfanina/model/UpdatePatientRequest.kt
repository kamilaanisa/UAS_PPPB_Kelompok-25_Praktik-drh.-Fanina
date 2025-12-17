package com.example.praktikdrhfanina.model

import com.google.gson.annotations.SerializedName

data class UpdatePatientRequest(
    @SerializedName("name")
    val username: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    val email: String,
    val password: String? = null
)
