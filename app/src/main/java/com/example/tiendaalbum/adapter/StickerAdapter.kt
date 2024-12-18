package com.example.tiendaalbum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiendaalbum.R
import com.example.tiendaalbum.model.Sticker

class StickerAdapter(private val stickers: List<Sticker>) : RecyclerView.Adapter<StickerAdapter.StickerViewHolder>() {

    class StickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSticker: ImageView = itemView.findViewById(R.id.ivSticker)
        val tvStickerNumber: TextView = itemView.findViewById(R.id.tvStickerNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sticker, parent, false)
        return StickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        val sticker = stickers[position]
        holder.tvStickerNumber.text = sticker.number.toString()

        // Cargar la imagen del sticker usando Glide
        // Asumiendo que tienes las imágenes almacenadas en res/drawable con nombres sticker1.jpg, sticker2.jpg, etc.
        val imageName = "sticker${sticker.number}"
        val imageResource = holder.itemView.context.resources.getIdentifier(imageName, "drawable", holder.itemView.context.packageName)
        if (imageResource != 0) {
            Glide.with(holder.itemView.context)
                .load(imageResource)
                .placeholder(R.drawable.placeholder) // Asegúrate de tener una imagen placeholder
                .into(holder.ivSticker)
        } else {
            holder.ivSticker.setImageResource(R.drawable.placeholder) // Imagen por defecto si no se encuentra la imagen
        }
    }

    override fun getItemCount(): Int {
        return stickers.size
    }
}
