package com.example.tiendaalbum.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendaalbum.R

class MainActivity : AppCompatActivity() {

    private var username: String? = null // Declarar la variable a nivel de clase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = intent.getStringExtra("USERNAME")
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        tvWelcome.text = "¡Bienvenido, $username!"

        val btnComprarAlbum = findViewById<Button>(R.id.btnComprarAlbum)
        val btnComprarSobre = findViewById<Button>(R.id.btnComprarSobre)
        val btnVerCatalogo = findViewById<Button>(R.id.btnVerCatalogo)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnComprarAlbum.setOnClickListener {
            // Navegar a la actividad de compra de álbumes
            val intent = Intent(this, ComprarAlbumActivity::class.java)
            intent.putExtra("USERNAME", username) // Pasar el USERNAME
            startActivity(intent)
        }

        btnComprarSobre.setOnClickListener {
            // Navegar a la actividad de compra de sobres
            val intent = Intent(this, ComprarSobreActivity::class.java)
            intent.putExtra("USERNAME", username) // Pasar el USERNAME
            startActivity(intent)
        }

        btnVerCatalogo.setOnClickListener {
            // Navegar a la actividad del catálogo de stickers
            val intent = Intent(this, CatalogoStickersActivity::class.java)
            intent.putExtra("USERNAME", username) // Pasar el USERNAME
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            // Navegar a LoginActivity y finalizar MainActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Limpiar el stack de actividades
            startActivity(intent)
            finish()
        }
    }
}
