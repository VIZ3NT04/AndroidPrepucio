package com.example.proyecto.fragments

import com.example.proyecto.api.Product

interface ProductsListener {
    fun onProductSelected(p: Product)
}