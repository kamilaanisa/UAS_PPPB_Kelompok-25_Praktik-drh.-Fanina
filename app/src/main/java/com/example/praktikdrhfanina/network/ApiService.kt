package com.example.praktikdrhfanina.network

import com.example.praktikdrhfanina.model.CreatePatientRequest
import com.example.praktikdrhfanina.model.LoginRequest
import com.example.praktikdrhfanina.model.LoginResponse
import com.example.praktikdrhfanina.model.UpdatePatientRequest
import com.example.praktikdrhfanina.model.User
import okhttp3.Request
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("patients")
    suspend fun getAllPatients(
        @Header("Authorization") token: String
    ): Response<List<User>>

    @POST("patients")
    suspend fun createPatient(
        @Header("Authorization") token: String,
        @Body request: CreatePatientRequest
    ): Response<User>

    @PUT("patients/{id}")
    suspend fun updatePatient (
        @Header("Authorization") token: String,
        @Path("id") patientId: Int,
        @Body request: UpdatePatientRequest
    ): Response<User>
}