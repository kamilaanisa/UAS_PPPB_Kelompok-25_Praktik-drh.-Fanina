package com.example.praktikdrhfanina.model

import com.google.gson.annotations.SerializedName

data class JenisHewan(
    @SerializedName("id_jenisHewan")
    val id: Int = 0,
    @SerializedName("nama_jenis")
    val nama_jenis: String = "",
    @SerializedName("id_pasien")
    val id_pasien: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
)
