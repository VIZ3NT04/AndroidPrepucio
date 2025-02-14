package com.example.proyecto.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.R
import com.example.proyecto.api.Product
import com.example.proyecto.api.User
import com.example.proyecto.databinding.ActivityMainBinding
import com.example.proyecto.databinding.ActivityProductBinding

class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProductBinding.inflate(layoutInflater)
        product = intent.getSerializableExtra("Product") as Product

        setContentView(binding.root)


        binding.priceProd.text = product.price.toString()
        binding.nameProd.text = product.name
        binding.nameUser.text = product.user.name
        binding.textProd.text = product.description


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}