package com.jengger.githubuserapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.jengger.githubuserapp.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.jengger.githubuserapp.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.jengger.githubuserapp.db.DatabaseContract.UserColumns.Companion._ID
import java.sql.SQLException

class UserHelper (context: Context){
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: UserHelper? = null
        fun getInstance(context: Context): UserHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: UserHelper(context)
                }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$_ID ASC")
    }

    // mencari berdasarkan username
    fun queryByUsername(username: String): Cursor {
        return database.query(
                    DATABASE_TABLE,
                    null,
                    "$USERNAME = ?",
                    arrayOf(username),
                    null,
                    null,
                    null,
                    null)
    }

    // menambahkan data
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    // mengupdate data
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    // menghapus data
    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)
    }
}