package com.example.praktikdrhfanina.model

import com.google.gson.annotations.SerializedName

data class CreateJenisHewanRequest(
    @SerializedName("nama_jenis")
    val nama_jenis: String,

    @SerializedName("id_pasien")
    val id_pasien: Int? = null
)
