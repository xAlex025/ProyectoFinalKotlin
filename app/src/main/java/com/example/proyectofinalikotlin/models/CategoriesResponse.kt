package com.example.proyectofinalikotlin.models

data class CategoriesResponse(
    val get: String,
    val parameters: List<String>,
    val errors: List<String>,
    val results: Int,
    val response: List<String>
)