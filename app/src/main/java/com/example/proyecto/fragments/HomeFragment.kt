package com.example.proyecto.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyecto.R
import com.example.proyecto.activities.CategoriesActivity
import com.example.proyecto.adapters.OnClickListener
import com.example.proyecto.adapters.ProductsAdapter
import com.example.proyecto.adapters.ProductsListener
import com.example.proyecto.api.Product
import com.example.proyecto.api.RetrofitInstance
import com.example.proyecto.api.User
import com.example.proyecto.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var listener: ProductsListener
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("User") as User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val iconCat = binding.icon

        iconCat.setOnClickListener {
            val intent = Intent(context, CategoriesActivity::class.java)
            intent.putExtra("User", user)
            startActivity(intent)
        }

        var products:List<Product> = listOf()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val allProducts = RetrofitInstance.api.listProducts()

                // Filtrar productos que no pertenecen al usuario
                val filteredProducts = allProducts.filter { it.usuario.id != user.id }

                // Actualizar la UI en el hilo principal
                requireActivity().runOnUiThread {
                    productsAdapter = ProductsAdapter(filteredProducts, this@HomeFragment)
                    binding.recyclerProducts.adapter = productsAdapter
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("Registro", "Error HTTP ${e.code()}: $errorBody")
            }
        }


        Thread.sleep(200)

        gridLayoutManager = GridLayoutManager(context,2)
        productsAdapter = ProductsAdapter(products, this)

        binding.recyclerProducts.apply {
            layoutManager = gridLayoutManager
            adapter = productsAdapter
        }

        return binding.root
    }

    fun setProductsListener(listener: ProductsListener) {
        this.listener = listener
    }

    override fun onClick(obj: Any) {
        val product: Product = obj as Product

        if (listener != null) {
            listener.onProductSelected(product)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(u: User) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("User", u)
                }
            }
    }
}
