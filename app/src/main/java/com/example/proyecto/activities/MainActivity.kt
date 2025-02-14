package com.example.proyecto.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.R
import com.example.proyecto.api.User
import com.example.proyecto.databinding.ActivityMainBinding
import com.example.proyecto.fragments.FavoritesFragment
import com.example.proyecto.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
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
                    /*val frgRoom: RoomFragment =
                        RoomFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedor, frgRoom)
                        .commit()*/
                }

                R.id.nav_message -> {
                    /*val frgRoom: RoomFragment =
                        RoomFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedor, frgRoom)
                        .commit()*/
                }
            }
            false
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}