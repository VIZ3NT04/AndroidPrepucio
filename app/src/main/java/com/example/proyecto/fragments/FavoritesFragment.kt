package com.example.proyecto.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyecto.R
import com.example.proyecto.adapters.FavProductAdapter
import com.example.proyecto.adapters.OnClickListener
import com.example.proyecto.adapters.ProductsAdapter
import com.example.proyecto.adapters.ProductsListener
import com.example.proyecto.api.Product
import com.example.proyecto.api.ProductEntity
import com.example.proyecto.api.RetrofitInstance
import com.example.proyecto.api.User
import com.example.proyecto.database.ProductApplication
import com.example.proyecto.databinding.FragmentFavoritesBinding
import com.example.proyecto.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FavoritesFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var productsAdapter: FavProductAdapter
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
    ): View? {

        var products:List<ProductEntity> = listOf()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                products = ProductApplication.database.productDao().getAllProduct()

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("Favoritos", "Error HTTP ${e.code()}: $errorBody")
            }
        }

        Thread.sleep(200)

        gridLayoutManager = GridLayoutManager(context,2)
        productsAdapter = FavProductAdapter(products, this)

        binding.recyclerProductsFav.apply {
            layoutManager = gridLayoutManager
            adapter = productsAdapter
        }


        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(u: User) =
            FavoritesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("User", u)
                }
            }
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
}