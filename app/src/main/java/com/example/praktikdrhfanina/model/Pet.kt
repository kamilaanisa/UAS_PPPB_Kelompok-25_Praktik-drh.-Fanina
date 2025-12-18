package com.example.praktikdrhfanina.model

import com.google.gson.annotations.SerializedName

data class Pet(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("nama_hewan")
    val nama_hewan: String = "",
    @SerializedName("umur")
    val umur: Int? = null,
    @SerializedName("jenis_hewan")
    val jenis_hewan: JenisHewan? = null
)

