package com.hincho.saborapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hincho.saborapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBienvenida.text = "Bienvenido a SaborApp"

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