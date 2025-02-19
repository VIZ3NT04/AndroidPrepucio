package com.example.proyecto.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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


    @Multipart
    @POST("uploads/imagen/{articuloId}")
    fun subirImagen(
        @Path("articuloId") articuloId: Int,
        @Part file: MultipartBody.Part
    ): Call<Map<String, String>>

    @GET("uploads/{id}")
    fun cargarImagen(@Path("id") id: Int): String

    //@PUT()
}