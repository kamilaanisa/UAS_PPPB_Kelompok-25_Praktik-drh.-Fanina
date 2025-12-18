package com.example.praktikdrhfanina.network

import com.example.praktikdrhfanina.model.CreateJenisHewanRequest
import com.example.praktikdrhfanina.model.CreatePatientRequest
import com.example.praktikdrhfanina.model.JenisHewan
import com.example.praktikdrhfanina.model.LoginRequest
import com.example.praktikdrhfanina.model.LoginResponse
import com.example.praktikdrhfanina.model.UpdateJenisHewanRequest
import com.example.praktikdrhfanina.model.UpdatePatientRequest
import com.example.praktikdrhfanina.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("api/patients")
    suspend fun getAllPatients(
        @Header("Authorization") token: String
    ): Response<List<User>>

    @POST("api/patients")
    suspend fun createPatient(
        @Header("Authorization") token: String,
        @Body request: CreatePatientRequest
    ): Response<User>

    @PUT("api/patients/{id}")
    suspend fun updatePatient (
        @Header("Authorization") token: String,
        @Path("id") patientId: Int,
        @Body request: UpdatePatientRequest
    ): Response<User>

    @DELETE("api/patients/{id}")
    suspend fun deletePatient(
        @Header("Authorization") token: String,
        @Path("id") patientId: Int
    ): Response<Void>

    @GET("api/jenis-hewan")
    suspend fun getAllJenisHewan(
        @Header("Authorization") token: String
    ): Response<List<JenisHewan>>

    @POST("api/jenis-hewan")
    suspend fun createJenisHewan(
        @Header("Authorization") token: String,
        @Body request: CreateJenisHewanRequest
    ): Response<JenisHewan>

    @PUT("api/jenis-hewan/{id}")
    suspend fun updateJenisHewan (
        @Header("Authorization") token: String,
        @Path("id") jenisHewanId: Int,
        @Body request: UpdateJenisHewanRequest
    ): Response<JenisHewan>

    @DELETE("api/jenis-hewan/{id}")
    suspend fun deleteJenisHewan(
        @Header("Authorization") token: String,
        @Path("id") jenisHewanId: Int
    ): Response<Void>
}