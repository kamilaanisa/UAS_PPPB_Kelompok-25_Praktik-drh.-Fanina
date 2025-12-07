package com.example.praktikdrhfanina

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.praktikdrhfanina.databinding.ActivityRegisterBinding
import com.example.praktikdrhfanina.model.CreatePatientRequest
import com.example.praktikdrhfanina.network.ApiClient
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Klik Login
        binding.textLoginSekarang.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Klik Register
        binding.buttonRegister.setOnClickListener {
            val username = binding.editTextUsername.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val phoneNumber = ""

            // Validasi input sederhana
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = ApiClient.getInstance().createPatient(
                        token = "Bearer YOUR_ADMIN_TOKEN", // pakai token admin jika diperlukan
                        request = CreatePatientRequest(
                            username = username,
                            phoneNumber = phoneNumber,
                            email = email,
                            password = password
                        )
                    )

                    val statusCode = response.code()
                    Log.d("CekStatusServer", "Register Response Code: $statusCode")

                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@RegisterActivity, "Register berhasil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Register gagal: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
