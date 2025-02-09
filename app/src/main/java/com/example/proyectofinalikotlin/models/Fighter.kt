package com.example.proyectofinal.model

import com.google.gson.annotations.SerializedName

data class Fighter(
    val id: Int,
    val name: String,
    val nickname: String?,
    val photo: String?,
    val gender: String,

    @SerializedName("birth_date")
    val birthDate: String?,

    val age: Int?,
    val height: String?,
    val weight: String?,
    val reach: String?,
    val stance: String?,
    val category: String?,

    val team: Team?,  // Relación con el equipo

    @SerializedName("last_update")
    val lastUpdate: String? // Última actualización
)

data class Team(
    val id: Int,
    val name: String
)
