package com.example.proyectofinal.model

data class FighterRecordResponse(
    val get: String,
    val parameters: Parameters,
    val errors: List<String>,
    val results: Int,
    val response: List<FighterRecord>
)


data class FighterRecord(
    val fighter: FighterInfo,
    val total: RecordStats,
    val ko: RecordStats,
    val sub: RecordStats
)

data class FighterInfo(
    val id: Int,
    val name: String,
    val photo: String
)

data class RecordStats(
    val win: Int,
    val loss: Int,
    val draw: Int? = 0
)
