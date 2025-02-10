package com.example.proyecto.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.R
import com.example.proyecto.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val txtEmail = binding.txtEmail.text.toString()
            val txtPassword = binding.txtPassword.text.toString()
            val txtName = binding.txtName.text.toString()

            var correct = true

            if (txtEmail.isEmpty()) {
                binding.txtEmail.error = "El email no puede estar vacio"
                correct = false
            }

            if (txtPassword.isEmpty()) {
                binding.txtPassword.error = "La contraseña no puede estar vacia"
                correct = false
            }

            if (txtName.isEmpty()) {
                binding.txtName.error = "El nombre no puede estar vacio"
                correct = false
            }

            if (correct) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnLogIn.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}