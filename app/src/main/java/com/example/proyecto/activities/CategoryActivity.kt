package com.example.proyecto.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyecto.R
import com.example.proyecto.adapters.CategoryAdapter
import com.example.proyecto.adapters.OnClickListener
import com.example.proyecto.adapters.ProductsAdapter
import com.example.proyecto.adapters.ProductsListener
import com.example.proyecto.api.Category
import com.example.proyecto.api.Product
import com.example.proyecto.api.RetrofitInstance
import com.example.proyecto.api.User
import com.example.proyecto.databinding.ActivityCategoriesBinding
import com.example.proyecto.databinding.ActivityCategoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CategoryActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        category = intent.getSerializableExtra("Category") as Category

        setContentView(binding.root)

        val iconBack = binding.icnBack

        iconBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        gridLayoutManager = GridLayoutManager(this,2)

        var products:List<Product> = listOf()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                products = RetrofitInstance.api.listProductsCategory(category)

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("Category", "Error HTTP ${e.code()}: $errorBody")
            }
        }

        productsAdapter = ProductsAdapter(products, this)

        binding.recyclerProducts.apply {
            layoutManager = gridLayoutManager
            adapter = productsAdapter
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onClick(obj: Any) {
        val product: Product = obj as Product

        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("Product", product)
        startActivity(intent)
    }


}