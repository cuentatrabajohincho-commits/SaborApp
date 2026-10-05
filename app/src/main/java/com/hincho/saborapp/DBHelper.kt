package com.hincho.saborapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "saborapp.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_USUARIO = "usuario"

        const val COL_ID = "id"
        const val COL_USUARIO = "usuario"
        const val COL_CLAVE = "clave"
        const val COL_ROL = "rol"
    }

    override fun onCreate(db: SQLiteDatabase) {

        val crearTablaUsuario = """
            CREATE TABLE $TABLE_USUARIO (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USUARIO TEXT NOT NULL UNIQUE,
                $COL_CLAVE TEXT NOT NULL,
                $COL_ROL TEXT NOT NULL
            )
        """.trimIndent()

// Usuario administrador inicial
        db.execSQL(
            """
    INSERT INTO $TABLE_USUARIO 
    ($COL_USUARIO, $COL_CLAVE, $COL_ROL)
    VALUES ('admin', '1234', 'ADMIN')
    """.trimIndent()
        )

// Usuario mozo inicial
        db.execSQL(
            """
    INSERT INTO $TABLE_USUARIO 
    ($COL_USUARIO, $COL_CLAVE, $COL_ROL)
    VALUES ('mozo', '1234', 'MOZO')
    """.trimIndent()
        )

    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIO")
        onCreate(db)
    }
}