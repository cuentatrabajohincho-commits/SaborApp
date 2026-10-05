package com.hincho.saborapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.hincho.saborapp.databinding.ActivityMenuBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBienvenida.text = "Bienvenido a SaborApp"

        binding.btnPlatos.setOnClickListener {
            // Próximamente: PlatosActivity
        }

        binding.btnMesas.setOnClickListener {
            // Próximamente: MesasActivity
        }

        binding.btnPedidos.setOnClickListener {
            // Próximamente: PedidoActivity
        }

        binding.btnReportes.setOnClickListener {
            // Próximamente: ReportesActivity
        }

        binding.btnSalir.setOnClickListener {
            finish()
        }
    }
}