package com.example.praktikdrhfanina.model

data class Patient(
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val createdDate: String,
    val pets: List<Pet> = emptyList()
)

