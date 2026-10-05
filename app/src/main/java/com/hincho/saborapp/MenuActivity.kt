package com.hincho.saborapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hincho.saborapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBienvenida.text = getString(R.string.bienvenido_saborapp)

        // Obtener el rol enviado desde LoginActivity
        val rol = intent.getStringExtra("rol") ?: "MOZO"

        // Si es MOZO, ocultar Reportes
        if (rol == "MOZO") {
            binding.btnReportes.visibility = View.GONE
        }

        binding.btnPlatos.setOnClickListener {
            startActivity(
                Intent(this, PlatosActivity::class.java)
            )
        }

        binding.btnMesas.setOnClickListener {
            startActivity(
                Intent(this, MesasActivity::class.java)
            )
        }

        binding.btnPedidos.setOnClickListener {
            startActivity(
                Intent(this, PedidoActivity::class.java)
            )
        }

        binding.btnReportes.setOnClickListener {
            startActivity(
                Intent(this, ReportesActivity::class.java)
            )
        }

        binding.btnSalir.setOnClickListener {
            finish()
        }
    }
}