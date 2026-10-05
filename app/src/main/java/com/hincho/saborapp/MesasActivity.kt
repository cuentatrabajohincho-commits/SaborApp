package com.hincho.saborapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hincho.saborapp.databinding.ActivityMesasBinding

class MesasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMesasBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMesasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        cargarMesas()
    }

    private fun cargarMesas() {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT numero, estado
            FROM mesa
            ORDER BY numero
            """.trimIndent(),
            null
        )

        val contenedor = binding.contenedorMesas
        contenedor.removeAllViews()

        while (cursor.moveToNext()) {

            val numero = cursor.getInt(
                cursor.getColumnIndexOrThrow("numero")
            )

            val estado = cursor.getString(
                cursor.getColumnIndexOrThrow("estado")
            )

            val texto = TextView(this)

            texto.text = "Mesa $numero\nEstado: $estado"
            texto.textSize = 18f
            texto.setPadding(16, 16, 16, 16)

            contenedor.addView(texto)
        }

        cursor.close()
        db.close()
    }
}