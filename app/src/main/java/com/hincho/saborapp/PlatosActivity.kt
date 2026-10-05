package com.hincho.saborapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hincho.saborapp.databinding.ActivityPlatosBinding

class PlatosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlatosBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlatosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        cargarPlatos()
    }

    private fun cargarPlatos() {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT nombre, precio, categoria
            FROM plato
            ORDER BY id
            """.trimIndent(),
            null
        )

        val contenedor = binding.contenedorPlatos
        contenedor.removeAllViews()

        while (cursor.moveToNext()) {

            val nombre = cursor.getString(
                cursor.getColumnIndexOrThrow("nombre")
            )

            val precio = cursor.getDouble(
                cursor.getColumnIndexOrThrow("precio")
            )

            val categoria = cursor.getString(
                cursor.getColumnIndexOrThrow("categoria")
            )

            val texto = TextView(this)

            texto.text = "$nombre\nS/ %.2f\nCategoría: $categoria".format(precio)
            texto.textSize = 18f
            texto.setPadding(16, 16, 16, 16)

            contenedor.addView(texto)
        }

        cursor.close()
        db.close()
    }
}