package com.example.tiendaalbum.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tiendaalbum.model.Sticker

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "sticker_app.db"
        private const val DATABASE_VERSION = 1

        // Tabla de Usuarios
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"

        // Tabla de Álbumes
        const val TABLE_ALBUMS = "albums"
        const val COLUMN_ALBUM_ID = "album_id"
        const val COLUMN_ALBUM_NAME = "album_name"
        const val COLUMN_USER_ID_FOREIGN = "user_id"

        // Tabla de Stickers
        const val TABLE_STICKERS = "stickers"
        const val COLUMN_STICKER_ID = "sticker_id"
        const val COLUMN_STICKER_NUMBER = "sticker_number"
        const val COLUMN_USER_ID_STICKER = "user_id_sticker"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla de Usuarios
        val CREATE_USERS_TABLE = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT" + ")")
        db?.execSQL(CREATE_USERS_TABLE)

        // Crear tabla de Álbumes
        val CREATE_ALBUMS_TABLE = ("CREATE TABLE " + TABLE_ALBUMS + "("
                + COLUMN_ALBUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ALBUM_NAME + " TEXT,"
                + COLUMN_USER_ID_FOREIGN + " INTEGER,"
                + "FOREIGN KEY($COLUMN_USER_ID_FOREIGN) REFERENCES $TABLE_USERS($COLUMN_USER_ID))")
        db?.execSQL(CREATE_ALBUMS_TABLE)

        // Crear tabla de Stickers
        val CREATE_STICKERS_TABLE = ("CREATE TABLE " + TABLE_STICKERS + "("
                + COLUMN_STICKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STICKER_NUMBER + " INTEGER,"
                + COLUMN_USER_ID_STICKER + " INTEGER,"
                + "FOREIGN KEY($COLUMN_USER_ID_STICKER) REFERENCES $TABLE_USERS($COLUMN_USER_ID))")
        db?.execSQL(CREATE_STICKERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Eliminar tablas existentes
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STICKERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ALBUMS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Método para agregar un nuevo usuario
    fun addUser(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_PASSWORD, password)

        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    // Método para verificar si el usuario existe
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Método para obtener el ID del usuario
    fun getUserId(username: String): Int? {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_USER_ID FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username))
        var userId: Int? = null
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
        }
        cursor.close()
        db.close()
        return userId
    }

    // Método para agregar una compra de álbum
    fun comprarAlbum(userId: Int, albumName: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ALBUM_NAME, albumName)
        values.put(COLUMN_USER_ID_FOREIGN, userId)

        val result = db.insert(TABLE_ALBUMS, null, values)
        db.close()
        return result != -1L
    }

    // Método para obtener los álbumes que posee un usuario
    fun getAlbumsByUser(userId: Int): List<String> {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_ALBUM_NAME FROM $TABLE_ALBUMS WHERE $COLUMN_USER_ID_FOREIGN = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val albums = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                val albumName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALBUM_NAME))
                albums.add(albumName)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return albums
    }

    // Método para guardar stickers obtenidos
    fun guardarStickers(userId: Int, stickers: List<Int>): Boolean {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            for (sticker in stickers) {
                val values = ContentValues()
                values.put(COLUMN_STICKER_NUMBER, sticker)
                values.put(COLUMN_USER_ID_STICKER, userId)
                db.insert(TABLE_STICKERS, null, values)
            }
            db.setTransactionSuccessful()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    // Método para obtener stickers por usuario
    fun getStickersByUser(userId: Int): List<Sticker> {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_STICKER_NUMBER FROM $TABLE_STICKERS WHERE $COLUMN_USER_ID_STICKER = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val stickers = mutableListOf<Sticker>()
        if (cursor.moveToFirst()) {
            do {
                val stickerNumber = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STICKER_NUMBER))
                stickers.add(Sticker(stickerNumber, stickerNumber))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return stickers
    }
}
