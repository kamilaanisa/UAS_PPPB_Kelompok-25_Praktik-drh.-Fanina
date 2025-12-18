package com.example.praktikdrhfanina.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("hewans")
    val hewans: List<Pet> = emptyList(),

    @SerializedName("jenis_hewan")
    val jenis_hewan: List<JenisHewan> = emptyList()
)

