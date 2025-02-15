package com.example.proyecto.api

import java.io.Serializable

data class Product(
    val name:String,
    val description:String,
    val category:Category,
    val price:Float,
    val user:User,
    val antiquity:String,
    val maps:String,
    //val etiqueta:List<String>,
    //val foto:List<Photo>
) : Serializable

data class Category(
    val id:Int,
    val name:String,
    val description:String
): Serializable

data class Photo(
    val id:Int,
    val url:String
): Serializable