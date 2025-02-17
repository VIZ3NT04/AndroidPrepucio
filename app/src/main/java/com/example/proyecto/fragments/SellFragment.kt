package com.example.proyecto.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.proyecto.R
import com.example.proyecto.api.Category
import com.example.proyecto.api.Product
import com.example.proyecto.api.RetrofitInstance
import com.example.proyecto.api.User
import com.example.proyecto.databinding.FragmentSellBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SellFragment : Fragment() {
    private lateinit var binding: FragmentSellBinding
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
        binding = FragmentSellBinding.inflate(inflater, container, false)

        val spinner: Spinner = binding.spCategory


        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val categories = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.listCategories()
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    categories)
                spinner.adapter = adapter

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("Registro", "Error HTTP ${e.code()}: $errorBody")
            }
        }


        binding.btnSell.setOnClickListener {
            val txtDesc = binding.txtDescProd.text.toString()
            val txtPrice = binding.txtPriceProd.text.toString()
            val txtName = binding.txtNameProd.text.toString()
            val txtAnt = binding.txtAntiquity.text.toString()
            val txtCategory = binding.spCategory.selectedItem as Category

            var correct = true

            if (txtDesc.isEmpty()) {
                binding.txtDescProd.error = "La descripcion no puede estar vacia"
                correct = false
            }

            if (txtPrice.isEmpty()) {
                binding.txtPriceProd.error = "El precio no puede estar vacio"
                correct = false
            }

            if (txtName.isEmpty()) {
                binding.txtNameProd.error = "El nombre no puede estar vacio"
                correct = false
            }

            if (correct) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val newProduct = Product(
                            name = txtName,
                            description = txtDesc,
                            category = txtCategory,
                            price = txtPrice.toFloat(),
                            user = user,
                            antiquity = txtAnt
                        )

                        val product = RetrofitInstance.api.addProduct(newProduct)

                        Thread {
                            Toast.makeText(
                                context,
                                "Se ha añadido el producto: ${product.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        Log.e("Registro", "Error HTTP ${e.code()}: $errorBody")
                    }
                }

            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(u: User) =
            SellFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("User", u)
                }
            }
    }
}
