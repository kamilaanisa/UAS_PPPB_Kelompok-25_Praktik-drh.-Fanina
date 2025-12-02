package com.example.praktikdrhfanina

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.praktikdrhfanina.databinding.ActivityLoginBinding
import com.example.praktikdrhfanina.model.LoginRequest
import kotlinx.coroutines.launch
import com.example.praktikdrhfanina.model.LoginResponse
import com.example.praktikdrhfanina.network.ApiClient

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        with(binding) {
            buttonLogin.setOnClickListener {
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Email dan password tidak boleh kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    try {
                        val response = ApiClient.getInstance().login(LoginRequest(email, password))

                        if (response.isSuccessful && response.body() != null) {
                            val loginData = response.body()!!
                            saveToken(loginData.token)

                            Toast.makeText(
                                this@LoginActivity,
                                "Login berhasil",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Login gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Gagal terhubung ke server: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            buttonGoogle.setOnClickListener {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }

            textDaftarSekarang.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun saveToken(token: String) {
        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        sharedPref.edit().putString("TOKEN", token).apply()
    }
}
