package com.example.praktikdrhfanina.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Pastikan link ini sudah HTTPS (ngrok punya Rakai)
    private const val BASE_URL = "https://mickey-unmilled-spousally.ngrok-free.dev/"

    fun getInstance(): ApiService {
        // 1. Alat pencatat log (biar kelihatan di Logcat)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 2. Settingan khusus Mobile (biar server gak bingung)
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-Requested-With", "Mobile") // Kasih tau ini HP
                    .addHeader("Accept", "application/json") // Minta data JSON
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()

        // 3. Gabungkan semuanya
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}