package com.example.praktikdrhfanina

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.TextView


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Sembunyikan Action Bar di atas
        supportActionBar?.hide()

        // Buat cari komponen TextView "Login" berdasarkan ID
        val textLogin: TextView = findViewById(R.id.textLoginSekarang)

        // Buat bisa di-klik
        textLogin.setOnClickListener {
            // Buat pindah kembali ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Buat tutup halaman register agar tidak menumpuk
            finish()
        }

        // ---
        // di sini  buat bang rakai tambahin kode untuk buttonRegister.setOnClickListener
        // yang terhubung ke backend, SEMANGAT YAA BE!! wkwkwkwkw
        // ---
    }
}