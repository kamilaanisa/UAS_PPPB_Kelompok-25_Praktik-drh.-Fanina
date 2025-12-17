package com.example.praktikdrhfanina

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.praktikdrhfanina.databinding.ActivityLoginBinding
import com.example.praktikdrhfanina.model.LoginRequest
import com.example.praktikdrhfanina.model.User
import com.example.praktikdrhfanina.network.ApiClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sembunyikan ActionBar biar tampilan full
        supportActionBar?.hide()

        with(binding) {

            // 1. Tombol Login
            buttonLogin.setOnClickListener {
                val email = editTextEmail.text.toString().trim()
                val password = editTextPassword.text.toString().trim()

                // Validasi input kosong
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Mulai proses login ke server
                lifecycleScope.launch {
                    try {
                        val response = ApiClient.getInstance().login(LoginRequest(email, password))

                        // Log untuk lapor status ke Rakai
                        val statusCode = response.code()
                        Log.d("CekStatusServer", "Login Response Code: $statusCode")

                        if (response.isSuccessful && response.body() != null) {
                            val loginData = response.body()!!

                            // A. Simpan Token
                            saveToken(loginData.token)

                            // B. Simpan Data User (Sesuai model User.kt kamu)
                            saveUserData(loginData.user)

                            Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()

                            // C. Pindah ke Main Activity
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            // Hapus history login biar pas back gak balik ke login lagi
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            // Jika password salah atau user tidak ditemukan
                            val errorBody = response.errorBody()?.string()
                            Log.e("LoginError", "Error Body: $errorBody")
                            Toast.makeText(this@LoginActivity, "Login Gagal ($statusCode). Cek Email/Password.", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        // Jika koneksi putus atau server mati
                        Log.e("LoginException", "Exception: ${e.message}", e)
                        Toast.makeText(this@LoginActivity, "Gagal terhubung: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            // 2. Tombol Google (Dummy - Langsung masuk aja)
            buttonGoogle.setOnClickListener {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }

            // 3. Teks Daftar Sekarang (Pindah ke Register)
            textDaftarSekarang.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // Fungsi Simpan Token ke SharedPreferences
    private fun saveToken(token: String) {
        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        // Gunakan commit() biar dia nunggu sampai benar-benar tersimpan baru lanjut
        val isSuccess = sharedPref.edit().putString("TOKEN", token).commit()
        Log.d("TokenCheck", "Token Saved: $isSuccess | Token: $token")
    }

    // Fungsi Simpan Data User ke SharedPreferences
    private fun saveUserData(user: User) {
        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        sharedPref.edit().apply {
            putInt("USER_ID", user.id)
            putString("USER_NAME", user.username) // ✅ Pakai 'username' sesuai User.kt
            putString("USER_EMAIL", user.email)
            putString("USER_PHONE", user.phoneNumber) // ✅ Simpan No HP juga
            apply()
        }
    }
}