package com.example.proyecto.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.R
import com.example.proyecto.api.RetrofitInstance
import com.example.proyecto.api.User
import com.example.proyecto.databinding.ActivityRegisterBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

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
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val newUser = User(
                            name = txtName,
                            email = txtEmail,
                            password = txtPassword
                        )

                        val user = RetrofitInstance.api.registerUser(newUser)
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Gracias por registrarte, ${user.name}",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.putExtra("User", user)
                            startActivity(intent)
                        }
                    } catch (e: HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        Log.e("Registro", "Error HTTP ${e.code()}: $errorBody")

                        runOnUiThread {
                             Toast.makeText(
                                this@RegisterActivity,
                                "Ha habido un error en las autenticaciones",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


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