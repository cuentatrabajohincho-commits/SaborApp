package com.hincho.saborapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hincho.saborapp.databinding.ActivityLoginBinding

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
                binding.tilUsuario.error = getString(R.string.error_usuario)
                hayError = true
            }

            if (clave.isEmpty()) {
                binding.tilClave.error = getString(R.string.error_contrasena)
                hayError = true
            }

            if (hayError) {
                return@setOnClickListener
            }

            // Login temporal para Sprint 1
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
                    getString(R.string.credenciales_incorrectas),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}