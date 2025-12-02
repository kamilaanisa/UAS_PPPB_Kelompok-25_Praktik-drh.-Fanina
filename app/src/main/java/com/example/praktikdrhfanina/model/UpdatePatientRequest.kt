package com.example.praktikdrhfanina.model

data class UpdatePatientRequest(
    val username: String,
    val phoneNumber: String,
    val email: String,
    val password: String? = null
)
