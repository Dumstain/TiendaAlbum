package com.example.tiendaalbum.activity

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendaalbum.R
import com.example.tiendaalbum.database.DatabaseHelper
import kotlin.random.Random

class ComprarSobreActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private var username: String? = null
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprar_sobre)

        db = DatabaseHelper(this)

        // Obtener el nombre de usuario desde el Intent
        username = intent.getStringExtra("USERNAME")
        userId = username?.let { db.getUserId(it) }

        if (userId == null) {
            Toast.makeText(this, "Error al obtener el usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val btnComprarSobre = findViewById<Button>(R.id.btnComprarSobre)

        btnComprarSobre.setOnClickListener {
            comprarSobre()
        }
    }

    private fun comprarSobre() {
        if (userId == null) {
            Toast.makeText(this, "Error al obtener el usuario", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener los álbumes que el usuario posee
        val albums = db.getAlbumsByUser(userId!!)
        if (albums.isEmpty()) {
            Toast.makeText(this, "No tienes ningún álbum. Compra un álbum primero.", Toast.LENGTH_SHORT).show()
            return
        }

        // Definir los rangos según los álbumes
        val rangos = mutableListOf<IntRange>()
        for (album in albums) {
            when (album) {
                "Álbum 1" -> rangos.add(1..45)
                "Álbum 2" -> rangos.add(46..90)
                "Álbum 3" -> rangos.add(91..135)
                // Agrega más casos si hay más álbumes
            }
        }

        // Combinar los rangos
        val stickersDisponibles = rangos.flatten()

        if (stickersDisponibles.isEmpty()) {
            Toast.makeText(this, "No hay stickers disponibles para tus álbumes.", Toast.LENGTH_SHORT).show()
            return
        }

        // Seleccionar 5 stickers aleatorios
        val stickersObtenidos = mutableSetOf<Int>()
        while (stickersObtenidos.size < 5) {
            val sticker = stickersDisponibles[Random.nextInt(stickersDisponibles.size)]
            stickersObtenidos.add(sticker)
        }

        // Guardar los stickers obtenidos en la base de datos
        val success = db.guardarStickers(userId!!, stickersObtenidos.toList())
        if (success) {
            Toast.makeText(this, "Has obtenido stickers: ${stickersObtenidos.joinToString(", ")}", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Error al guardar los stickers", Toast.LENGTH_SHORT).show()
        }
    }
}
