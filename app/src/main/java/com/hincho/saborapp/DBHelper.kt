package com.hincho.saborapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "saborapp.db"
        private const val DATABASE_VERSION = 5

        const val TABLE_USUARIO = "usuario"

        const val COL_ID = "id"
        const val COL_USUARIO = "usuario"
        const val COL_CLAVE = "clave"
        const val COL_ROL = "rol"
    }

    override fun onCreate(db: SQLiteDatabase) {

        // Tabla usuario
        val crearTablaUsuario = """
            CREATE TABLE $TABLE_USUARIO (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USUARIO TEXT NOT NULL UNIQUE,
                $COL_CLAVE TEXT NOT NULL,
                $COL_ROL TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(crearTablaUsuario)

        // Tabla plato
        val crearTablaPlato = """
            CREATE TABLE plato (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                precio REAL NOT NULL,
                categoria TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(crearTablaPlato)

        // Platos iniciales
        db.execSQL(
            """
    INSERT INTO plato (nombre, precio, categoria)
    VALUES ('Pollo a la brasa', 25.00, 'Platos')
    """.trimIndent()
        )

        db.execSQL(
            """
    INSERT INTO plato (nombre, precio, categoria)
    VALUES ('1/4 de pollo', 15.00, 'Platos')
    """.trimIndent()
        )

        db.execSQL(
            """
    INSERT INTO plato (nombre, precio, categoria)
    VALUES ('Chicha morada', 5.00, 'Bebidas')
    """.trimIndent()
        )

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
        if (oldVersion < 2) {
            db.execSQL(
                """
                CREATE TABLE plato (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    precio REAL NOT NULL,
                    categoria TEXT NOT NULL
                )
                """.trimIndent()
            )
        }

        if (oldVersion < 3) {
            db.execSQL(
                """
            INSERT INTO plato (nombre, precio, categoria)
            VALUES ('Pollo a la brasa', 25.00, 'Platos')
            """.trimIndent()
            )

            db.execSQL(
                """
            INSERT INTO plato (nombre, precio, categoria)
            VALUES ('1/4 de pollo', 15.00, 'Platos')
            """.trimIndent()
            )

            db.execSQL(
                """
            INSERT INTO plato (nombre, precio, categoria)
            VALUES ('Chicha morada', 5.00, 'Bebidas')
            """.trimIndent()
            )
        }

        if (oldVersion < 4) {

            db.execSQL(
                """
        CREATE TABLE mesa (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            numero INTEGER NOT NULL UNIQUE,
            estado TEXT NOT NULL
        )
        """.trimIndent()
            )

            db.execSQL(
                """
        INSERT INTO mesa (numero, estado)
        VALUES (1, 'LIBRE')
        """.trimIndent()
            )

            db.execSQL(
                """
        INSERT INTO mesa (numero, estado)
        VALUES (2, 'LIBRE')
        """.trimIndent()
            )

            db.execSQL(
                """
        INSERT INTO mesa (numero, estado)
        VALUES (3, 'LIBRE')
        """.trimIndent()
            )

            db.execSQL(
                """
        INSERT INTO mesa (numero, estado)
        VALUES (4, 'LIBRE')
        """.trimIndent()
            )
        }

        if (oldVersion < 5) {

            db.execSQL(
                """
        CREATE TABLE pedido (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            mesa_id INTEGER NOT NULL,
            plato_id INTEGER NOT NULL,
            cantidad INTEGER NOT NULL,
            estado TEXT NOT NULL,
            FOREIGN KEY (mesa_id) REFERENCES mesa(id),
            FOREIGN KEY (plato_id) REFERENCES plato(id)
        )
        """.trimIndent()
            )
        }
    }
}