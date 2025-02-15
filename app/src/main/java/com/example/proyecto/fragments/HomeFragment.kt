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
    private lateinit var drawerLayout: DrawerLayout
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

        var products:List<Product> = listOf()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                products = RetrofitInstance.api.listProducts()

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("Registro", "Error HTTP ${e.code()}: $errorBody")
            }
        }

        drawerLayout = binding.main

        val navigationView: NavigationView = binding.navMenu
        navigationView.setCheckedItem(R.id.nav_home)

        val headerView = navigationView.getHeaderView(0)
        val txtUserName = headerView.findViewById<TextView>(R.id.userName)
        val txtUserEmail = headerView.findViewById<TextView>(R.id.userEmail)

        txtUserName.text = user.name
        txtUserEmail.text = user.email



        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(context, "Home seleccionado", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_settings -> {
                    Toast.makeText(context, "Configuración seleccionada", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val iconCat = binding.icon

        iconCat.setOnClickListener {
            val intent = Intent(context, CategoriesActivity::class.java)
            intent.putExtra("User", user)
            startActivity(intent)
        }

        val iconDrawer = binding.navDrawer
        iconDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }


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
