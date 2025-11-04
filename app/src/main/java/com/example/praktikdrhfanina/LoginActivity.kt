package com.example.praktikdrhfanina

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.TextView


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Buat sembunyikan Action Bar di atas
        supportActionBar?.hide()

        //Buat cari komponen TextView "Daftar sekarang" berdasarkan ID
        val textDaftar: TextView = findViewById(R.id.textDaftarSekarang)

        // Buat bisa di-klik
        textDaftar.setOnClickListener {
            // Buat Intent untuk pindah ke RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            //   halaman login tidak di-finish(), biar bisa balik lagi
        }

        // ---
        // di sini  buat bang rakai tambahin kode untuk buttonLogin.setOnClickListener
        // yang terhubung ke backend, SEMANGAT BE!! hehehehheeh
        // ---
    }
}