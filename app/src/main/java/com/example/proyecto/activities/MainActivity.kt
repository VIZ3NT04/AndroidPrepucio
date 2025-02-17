package com.example.proyecto.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.R
import com.example.proyecto.api.Product
import com.example.proyecto.api.User
import com.example.proyecto.databinding.ActivityMainBinding
import com.example.proyecto.fragments.FavoritesFragment
import com.example.proyecto.fragments.HomeFragment
import com.example.proyecto.adapters.ProductsListener
import com.example.proyecto.fragments.ProfileFragment
import com.example.proyecto.fragments.SellFragment

class MainActivity : AppCompatActivity(), ProductsListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        user = intent.getSerializableExtra("User") as User

        setContentView(binding.root)

        val frgHome: HomeFragment =
            HomeFragment.newInstance(user)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragments, frgHome)
            .commit()
        frgHome.setProductsListener(this)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            it.isChecked = true

            when (it.itemId) {
                R.id.nav_home -> {
                    val fragHome: HomeFragment =
                        HomeFragment.newInstance(user)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragments, fragHome)
                        .commit()
                }

                R.id.nav_fav -> {
                    val frgFav: FavoritesFragment =
                        FavoritesFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragments, frgFav)
                        .commit()
                }

                R.id.nav_sell -> {
                    val frgSell: SellFragment =
                        SellFragment.newInstance(user)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragments, frgSell)
                        .commit()
                }

                R.id.nav_message -> {
                    val frgProfile: ProfileFragment =
                        ProfileFragment.newInstance(user)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragments, frgProfile)
                        .commit()
                }
            }
            false
        }

/*
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
*/
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    override fun onProductSelected(product: Product) {
        if (product != null) {
            val intent = Intent(this, ProductActivity::class.java)
            intent.putExtra("Product", product)
            startActivity(intent)
        }
    }
}