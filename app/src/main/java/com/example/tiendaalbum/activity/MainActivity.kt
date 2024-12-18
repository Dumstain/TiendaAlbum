package com.example.tiendaalbum.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendaalbum.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = intent.getStringExtra("USERNAME")
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        tvWelcome.text = "¡Bienvenido, $username!"

        val btnComprarAlbum = findViewById<Button>(R.id.btnComprarAlbum)
        val btnComprarSobre = findViewById<Button>(R.id.btnComprarSobre)
        val btnVerCatalogo = findViewById<Button>(R.id.btnVerCatalogo)

        btnComprarAlbum.setOnClickListener {
            // Navegar a la actividad de compra de álbumes
            val intent = Intent(this, ComprarAlbumActivity::class.java)
            startActivity(intent)
        }

        btnComprarSobre.setOnClickListener {
            // Navegar a la actividad de compra de sobres
            val intent = Intent(this, ComprarSobreActivity::class.java)
            startActivity(intent)
        }

        btnVerCatalogo.setOnClickListener {
            // Navegar a la actividad del catálogo de stickers
            val intent = Intent(this, CatalogoStickersActivity::class.java)
            startActivity(intent)
        }
    }
}
