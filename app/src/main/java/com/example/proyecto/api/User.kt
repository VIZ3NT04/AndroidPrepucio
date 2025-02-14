package com.example.proyecto.api

import java.io.Serializable

/*
data class UsuarioResponse(
    val count:Int,
    val results:List<Usuario>
)
*/

data class User(
    val name:String,
    val email:String,
    val password:String
) : Serializable

