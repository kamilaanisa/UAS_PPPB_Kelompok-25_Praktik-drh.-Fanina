package com.example.praktikdrhfanina.model

data class CreatePatientRequest(
    val username: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
)
