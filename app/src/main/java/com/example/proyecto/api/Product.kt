package com.example.proyecto.api

data class Product(
    val id:Int,
    val name:String,
    val description:String,
    val category:Category,
    val price:Float,
    val user:User,
    val antiquity:String,
    val maps:String,
    //val etiqueta:List<String>,
    //val foto:List<Photo>
)

data class Category(
    val id:Int,
    val name:String,
    val description:String
)

data class Photo(
    val id:Int,
    val url:String
)