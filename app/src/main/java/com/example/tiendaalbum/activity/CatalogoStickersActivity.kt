package com.example.tiendaalbum.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendaalbum.R
import com.example.tiendaalbum.adapter.StickerAdapter
import com.example.tiendaalbum.database.DatabaseHelper
import com.example.tiendaalbum.model.Sticker

class CatalogoStickersActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private var username: String? = null
    private var userId: Int? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StickerAdapter
    private lateinit var stickerList: List<Sticker>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_stickers)

        db = DatabaseHelper(this)

        // Obtener el nombre de usuario desde el Intent
        username = intent.getStringExtra("USERNAME")
        userId = username?.let { db.getUserId(it) }

        if (userId == null) {
            Toast.makeText(this, "Error al obtener el usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Obtener la lista de stickers del usuario
        stickerList = db.getStickersByUser(userId!!)

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewStickers)
        recyclerView.layoutManager = GridLayoutManager(this, 4) // 4 columnas

        // Inicializar el adaptador
        adapter = StickerAdapter(stickerList)
        recyclerView.adapter = adapter
    }
}
