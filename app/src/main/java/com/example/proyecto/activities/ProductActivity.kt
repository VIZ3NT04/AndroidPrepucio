package com.example.proyecto.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.R
import com.example.proyecto.api.CategoryEntity
import com.example.proyecto.api.Product
import com.example.proyecto.api.ProductEntity
import com.example.proyecto.api.RetrofitInstance
import com.example.proyecto.api.User
import com.example.proyecto.api.UserEntity
import com.example.proyecto.database.ProductApplication
import com.example.proyecto.database.ProductDatabase
import com.example.proyecto.databinding.ActivityMainBinding
import com.example.proyecto.databinding.ActivityProductBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
        //binding.nameUser.text = product.user.name
        binding.textProd.text = product.description


        binding.btnBuy.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    RetrofitInstance.api.deleteProducto(product)

                    runOnUiThread {
                        Toast.makeText(
                            this@ProductActivity,
                            "Se ha comprado el producto: ${product.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    /// Aqui habría que hacer que el usuario pudiera dar una valoración
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("Producto", "Error HTTP ${e.code()}: $errorBody")
                }
            }
        }


        val iconHeart = binding.icnHeart
        iconHeart.setOnClickListener {
            iconHeart.setImageResource(R.drawable.ic_heart)

            Thread{

                val newUser = UserEntity(
                    name = product.user.name,
                    email = product.user.email,
                    password = product.user.password,
                    poblacion = product.user.poblacion
                )

                val newCategory = CategoryEntity(
                    id = product.category.id,
                    name = product.category.name,
                    description = product.category.description
                )

                val newProduct = ProductEntity(
                    name = product.name,
                    description = product.description,
                    category = newCategory,
                    price = product.price,
                    user = newUser,
                    antiquity = product.antiquity
                )
                ProductApplication.database.productDao().addProduct(newProduct)
            }.start()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}