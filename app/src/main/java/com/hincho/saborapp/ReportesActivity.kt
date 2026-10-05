package com.hincho.saborapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hincho.saborapp.databinding.ActivityReportesBinding

class ReportesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportesBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReportesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        cargarReporte()
    }

    private fun cargarReporte() {

        val db = dbHelper.readableDatabase

        // Total de pedidos
        val cursorTotal = db.rawQuery(
            """
            SELECT COUNT(*)
            FROM pedido
            """.trimIndent(),
            null
        )

        cursorTotal.moveToFirst()

        val totalPedidos = cursorTotal.getInt(0)

        cursorTotal.close()

        // Pedidos pendientes
        val cursorPendientes = db.rawQuery(
            """
            SELECT COUNT(*)
            FROM pedido
            WHERE estado = 'PENDIENTE'
            """.trimIndent(),
            null
        )

        cursorPendientes.moveToFirst()

        val pedidosPendientes = cursorPendientes.getInt(0)

        cursorPendientes.close()

        // Pedidos atendidos
        val cursorAtendidos = db.rawQuery(
            """
            SELECT COUNT(*)
            FROM pedido
            WHERE estado = 'ATENDIDO'
            """.trimIndent(),
            null
        )

        cursorAtendidos.moveToFirst()

        val pedidosAtendidos = cursorAtendidos.getInt(0)

        cursorAtendidos.close()

        // Total vendido
        val cursorVentas = db.rawQuery(
            """
            SELECT
                COALESCE(
                    SUM(pedido.cantidad * plato.precio),
                    0
                )
            FROM pedido
            INNER JOIN plato
                ON pedido.plato_id = plato.id
            WHERE pedido.estado = 'ATENDIDO'
            """.trimIndent(),
            null
        )

        cursorVentas.moveToFirst()

        val totalVendido = cursorVentas.getDouble(0)

        cursorVentas.close()

        binding.tvTotalPedidos.text =
            "Total de pedidos: $totalPedidos"

        binding.tvPedidosPendientes.text =
            "Pedidos pendientes: $pedidosPendientes"

        binding.tvPedidosAtendidos.text =
            "Pedidos atendidos: $pedidosAtendidos"

        binding.tvTotalVendido.text =
            "Total vendido: S/ %.2f".format(totalVendido)

        cargarVentasPorPlato(db)

        db.close()
    }

    private fun cargarVentasPorPlato(db: android.database.sqlite.SQLiteDatabase) {

        val cursor = db.rawQuery(
            """
            SELECT
                plato.nombre,
                SUM(pedido.cantidad) AS cantidad,
                SUM(pedido.cantidad * plato.precio) AS total
            FROM pedido
            INNER JOIN plato
                ON pedido.plato_id = plato.id
            WHERE pedido.estado = 'ATENDIDO'
            GROUP BY plato.id, plato.nombre
            ORDER BY total DESC
            """.trimIndent(),
            null
        )

        val contenedor =
            binding.contenedorReportePlatos

        contenedor.removeAllViews()

        while (cursor.moveToNext()) {

            val nombre =
                cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                )

            val cantidad =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow("cantidad")
                )

            val total =
                cursor.getDouble(
                    cursor.getColumnIndexOrThrow("total")
                )

            val texto = TextView(this)

            texto.text =
                "$nombre\n" +
                        "Cantidad vendida: $cantidad\n" +
                        "Total: S/ %.2f".format(total)

            texto.textSize = 18f
            texto.setPadding(16, 16, 16, 16)

            contenedor.addView(texto)
        }

        cursor.close()
    }
}