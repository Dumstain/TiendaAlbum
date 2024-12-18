package com.example.tiendaalbum.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendaalbum.R
import com.example.tiendaalbum.database.DatabaseHelper

class ComprarAlbumActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private var username: String? = null
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprar_album)

        db = DatabaseHelper(this)

        // Obtener el nombre de usuario desde el Intent
        username = intent.getStringExtra("USERNAME")
        userId = username?.let { db.getUserId(it) }

        if (userId == null) {
            Toast.makeText(this, "Error al obtener el usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Obtener referencias a los botones de los álbumes
        val btnAlbum1 = findViewById<Button>(R.id.btnAlbum1)
        val btnAlbum2 = findViewById<Button>(R.id.btnAlbum2)
        val btnAlbum3 = findViewById<Button>(R.id.btnAlbum3)

        // Configurar listeners para los botones
        btnAlbum1.setOnClickListener {
            comprarAlbum("Álbum 1")
        }

        btnAlbum2.setOnClickListener {
            comprarAlbum("Álbum 2")
        }

        btnAlbum3.setOnClickListener {
            comprarAlbum("Álbum 3")
        }
    }

    private fun comprarAlbum(albumName: String) {
        if (userId == null) {
            Toast.makeText(this, "Error al obtener el usuario", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar si el usuario ya ha comprado este álbum
        val albumsComprados = db.getAlbumsByUser(userId!!)
        if (albumsComprados.contains(albumName)) {
            Toast.makeText(this, "Ya has comprado este álbum", Toast.LENGTH_SHORT).show()
            return
        }

        // Registrar la compra en la base de datos
        val success = db.comprarAlbum(userId!!, albumName)
        if (success) {
            Toast.makeText(this, "$albumName comprado exitosamente", Toast.LENGTH_SHORT).show()
            // Opcional: Navegar de vuelta a la MainActivity o actualizar la interfaz
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error al comprar $albumName", Toast.LENGTH_SHORT).show()
        }
    }
}
