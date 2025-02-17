package com.example.proyecto.api

import androidx.room.Delete
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiGrupoAService {

    @GET("usuarios/login")
    suspend fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String
    ): User

    @POST("usuarios")
    suspend fun registerUser(@Body user: User): User

    @POST("productos")
    suspend fun addProduct(@Body product: Product): Product

    @GET("productos")
    suspend fun listProducts(): List<Product>

    @GET("categorias")
    suspend fun listCategories(): List<Category>

    // ***********  Cambiar  ************//
    @DELETE("productos/eliminarProducto")
    suspend fun deleteProducto(@Body product: Product): Void

    // ***********  Cambiar  ************//
    @GET("productos/listarProductosPorCategoria")
    suspend fun listProductsCategory(@Body category: Category): List<Product>
}