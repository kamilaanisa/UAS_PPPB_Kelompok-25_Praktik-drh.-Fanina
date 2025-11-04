package com.example.praktikdrhfanina

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {

    // ini waktunya dalam milidetik (2500ms = 2.5 detik)
    private val SPLASH_TIME_OUT: Long = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menghubungkan layout XML (activity_splash.xml) ke file Kotlin ini
        setContentView(R.layout.activity_splash)

        // Sembunyikan Action Bar (atas) agar layar penuh
        supportActionBar?.hide()

        // Handler untuk menunda perpindahan ke layar berikutnya
        Handler(Looper.getMainLooper()).postDelayed({
            // Buat Intent untuk pindah ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Tutup SplashActivity biar gak bisa kembali dengan tombol "Back"
            finish()
        }, SPLASH_TIME_OUT)
    }
}