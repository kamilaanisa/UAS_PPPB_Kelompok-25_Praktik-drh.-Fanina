package com.example.praktikdrhfanina.utils

import android.content.Context

object TokenManager {
    fun getToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        return sharedPref.getString("TOKEN", null)
    }

    fun getAuthHeader(context: Context): String {
        val token = getToken(context)
        return "Bearer $token"
    }

    // Tambahan buat Log out nanti
    fun clearToken(context: Context) {
        val sharedPref = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }
}