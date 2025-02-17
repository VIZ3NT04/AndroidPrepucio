package com.example.proyecto.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitInstance {
    private const val BASE_URL = "http://api.grupoa.com:8080/MyApp/"
    val api: ApiGrupoAService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiGrupoAService::class.java)
    }
}