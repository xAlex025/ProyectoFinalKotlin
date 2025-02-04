package com.example.proyectofinalikotlin.models

data class SeasonsResponse(
    val get: String,
    val parameters: List<String>,
    val errors: List<String>,
    val results: Int,
    val response: List<Int>
)
