package com.example.proyectofinal.model

data class FighterResponse(
    val get: String,
    val parameters: Parameters,
    val errors: List<String>,
    val results: Int,
    val response: List<Fighter>
)

data class Parameters(
    val id: String
)