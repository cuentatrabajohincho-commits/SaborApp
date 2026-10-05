package com.hincho.saborapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hincho.saborapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

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

            // Consultar usuario en SQLite
            val db = dbHelper.readableDatabase

            val cursor = db.rawQuery(
                """
                SELECT usuario, rol
                FROM usuario
                WHERE usuario = ? AND clave = ?
                """.trimIndent(),
                arrayOf(usuario, clave)
            )

            if (cursor.moveToFirst()) {

                val usuarioEncontrado =
                    cursor.getString(cursor.getColumnIndexOrThrow("usuario"))

                val rol =
                    cursor.getString(cursor.getColumnIndexOrThrow("rol"))

                cursor.close()
                db.close()

                val intent = Intent(this, MenuActivity::class.java)

                intent.putExtra("usuario", usuarioEncontrado)
                intent.putExtra("rol", rol)

                startActivity(intent)

                // Evita regresar al Login con el botón Atrás
                finish()

            } else {

                cursor.close()
                db.close()

                Toast.makeText(
                    this,
                    getString(R.string.credenciales_incorrectas),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}