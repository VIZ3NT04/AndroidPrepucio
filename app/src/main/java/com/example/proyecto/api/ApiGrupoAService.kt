package com.example.proyecto.api

import retrofit2.http.Body
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

    @GET("productos")
    suspend fun listProducts(): List<Product>

    @GET("categorias")
    suspend fun listCategories(): List<Category>
}