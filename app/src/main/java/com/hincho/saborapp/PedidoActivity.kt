package com.hincho.saborapp

import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hincho.saborapp.databinding.ActivityPedidoBinding

class PedidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidoBinding
    private lateinit var dbHelper: DBHelper

    private val mesas = mutableListOf<Int>()
    private val platos = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        cargarMesas()
        cargarPlatos()
        cargarPedidos()

        binding.btnRegistrarPedido.setOnClickListener {
            registrarPedido()
        }
    }

    private fun cargarMesas() {

        mesas.clear()

        val nombresMesas = mutableListOf<String>()

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT id, numero, estado
            FROM mesa
            ORDER BY numero
            """.trimIndent(),
            null
        )

        while (cursor.moveToNext()) {

            val id = cursor.getInt(
                cursor.getColumnIndexOrThrow("id")
            )

            val numero = cursor.getInt(
                cursor.getColumnIndexOrThrow("numero")
            )

            val estado = cursor.getString(
                cursor.getColumnIndexOrThrow("estado")
            )

            mesas.add(id)

            nombresMesas.add(
                "Mesa $numero - $estado"
            )
        }

        cursor.close()
        db.close()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombresMesas
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spMesa.adapter = adapter
    }

    private fun cargarPlatos() {

        platos.clear()

        val nombresPlatos = mutableListOf<String>()

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT id, nombre
            FROM plato
            ORDER BY id
            """.trimIndent(),
            null
        )

        while (cursor.moveToNext()) {

            val id = cursor.getInt(
                cursor.getColumnIndexOrThrow("id")
            )

            val nombre = cursor.getString(
                cursor.getColumnIndexOrThrow("nombre")
            )

            platos.add(id)
            nombresPlatos.add(nombre)
        }

        cursor.close()
        db.close()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombresPlatos
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spPlato.adapter = adapter
    }

    private fun registrarPedido() {

        if (mesas.isEmpty() || platos.isEmpty()) {

            Toast.makeText(
                this,
                "No hay mesas o platos disponibles",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val cantidadTexto =
            binding.etCantidad.text.toString().trim()

        if (cantidadTexto.isEmpty()) {

            binding.etCantidad.error =
                "Ingrese una cantidad"

            return
        }

        val cantidad =
            cantidadTexto.toIntOrNull()

        if (cantidad == null || cantidad <= 0) {

            binding.etCantidad.error =
                "Ingrese una cantidad válida"

            return
        }

        val mesaId =
            mesas[binding.spMesa.selectedItemPosition]

        val platoId =
            platos[binding.spPlato.selectedItemPosition]

        val db = dbHelper.writableDatabase

        // Verificar que la mesa siga libre
        val cursorMesa = db.rawQuery(
            """
            SELECT estado
            FROM mesa
            WHERE id = ?
            """.trimIndent(),
            arrayOf(mesaId.toString())
        )

        if (cursorMesa.moveToFirst()) {

            val estadoMesa = cursorMesa.getString(0)

            if (estadoMesa != "LIBRE") {

                cursorMesa.close()
                db.close()

                Toast.makeText(
                    this,
                    "La mesa seleccionada está ocupada",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }
        }

        cursorMesa.close()

        // Registrar pedido
        db.execSQL(
            """
            INSERT INTO pedido
            (mesa_id, plato_id, cantidad, estado)
            VALUES (?, ?, ?, ?)
            """.trimIndent(),
            arrayOf(
                mesaId,
                platoId,
                cantidad,
                "PENDIENTE"
            )
        )

        // Cambiar mesa a ocupada
        db.execSQL(
            """
            UPDATE mesa
            SET estado = 'OCUPADA'
            WHERE id = ?
            """.trimIndent(),
            arrayOf(mesaId)
        )

        db.close()

        Toast.makeText(
            this,
            "Pedido registrado correctamente",
            Toast.LENGTH_SHORT
        ).show()

        binding.etCantidad.text?.clear()

        cargarMesas()
        cargarPedidos()
    }

    private fun cargarPedidos() {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT
                pedido.id,
                mesa.numero,
                plato.nombre,
                pedido.cantidad,
                pedido.estado
            FROM pedido
            INNER JOIN mesa
                ON pedido.mesa_id = mesa.id
            INNER JOIN plato
                ON pedido.plato_id = plato.id
            ORDER BY pedido.id DESC
            """.trimIndent(),
            null
        )

        val contenedor =
            binding.contenedorPedidos

        contenedor.removeAllViews()

        while (cursor.moveToNext()) {

            val id = cursor.getInt(
                cursor.getColumnIndexOrThrow("id")
            )

            val numeroMesa = cursor.getInt(
                cursor.getColumnIndexOrThrow("numero")
            )

            val nombrePlato = cursor.getString(
                cursor.getColumnIndexOrThrow("nombre")
            )

            val cantidad = cursor.getInt(
                cursor.getColumnIndexOrThrow("cantidad")
            )

            val estado = cursor.getString(
                cursor.getColumnIndexOrThrow("estado")
            )

            val fila = LinearLayout(this)

            fila.orientation =
                LinearLayout.VERTICAL

            fila.setPadding(
                16,
                16,
                16,
                16
            )

            val texto = TextView(this)

            texto.text =
                "Pedido #$id\n" +
                        "Mesa: $numeroMesa\n" +
                        "Plato: $nombrePlato\n" +
                        "Cantidad: $cantidad\n" +
                        "Estado: $estado"

            texto.textSize = 18f

            fila.addView(texto)

            if (estado == "PENDIENTE") {

                val botonAtender =
                    Button(this)

                botonAtender.text =
                    "Atender pedido"

                botonAtender.gravity =
                    Gravity.CENTER

                botonAtender.setOnClickListener {

                    atenderPedido(id)
                }

                fila.addView(botonAtender)
            }

            contenedor.addView(fila)
        }

        cursor.close()
        db.close()
    }

    private fun atenderPedido(
        pedidoId: Int
    ) {

        val db = dbHelper.writableDatabase

        val cursor = db.rawQuery(
            """
            SELECT mesa_id
            FROM pedido
            WHERE id = ?
            """.trimIndent(),
            arrayOf(pedidoId.toString())
        )

        if (!cursor.moveToFirst()) {

            cursor.close()
            db.close()

            Toast.makeText(
                this,
                "No se encontró el pedido",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val mesaId = cursor.getInt(
            cursor.getColumnIndexOrThrow("mesa_id")
        )

        cursor.close()

        // Cambiar pedido a atendido
        db.execSQL(
            """
            UPDATE pedido
            SET estado = 'ATENDIDO'
            WHERE id = ?
            """.trimIndent(),
            arrayOf(pedidoId)
        )

        // Liberar mesa
        db.execSQL(
            """
            UPDATE mesa
            SET estado = 'LIBRE'
            WHERE id = ?
            """.trimIndent(),
            arrayOf(mesaId)
        )

        db.close()

        Toast.makeText(
            this,
            "Pedido atendido. Mesa liberada.",
            Toast.LENGTH_SHORT
        ).show()

        cargarMesas()
        cargarPedidos()
    }
}