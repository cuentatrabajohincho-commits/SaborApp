package com.hincho.saborapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.hincho.saborapp.databinding.ActivityLoginBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIngresar.setOnClickListener {

            val usuario = binding.etUsuario.text.toString().trim()
            val clave = binding.etClave.text.toString().trim()

            // Limpiar errores anteriores
            binding.tilUsuario.error = null
            binding.tilClave.error = null

            var hayError = false

            if (usuario.isEmpty()) {
                binding.tilUsuario.error = "Ingrese su usuario"
                hayError = true
            }

            if (clave.isEmpty()) {
                binding.tilClave.error = "Ingrese su contraseña"
                hayError = true
            }

            if (hayError) {
                return@setOnClickListener
            }

            if (usuario == "admin" && clave == "1234") {

                val intent = Intent(this, MenuActivity::class.java)

                intent.putExtra("usuario", usuario)
                intent.putExtra("rol", "ADMIN")

                startActivity(intent)

                // Evita regresar al login con el botón Atrás
                finish()

            } else {

                Toast.makeText(
                    this,
                    "Credenciales incorrectas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}