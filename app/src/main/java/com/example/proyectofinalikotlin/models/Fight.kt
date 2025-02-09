package com.example.proyectofinalikotlin.models


data class FightResponse(
    val get: String ?= null,
    val parameters: Parameters ?= null,
    val results: Int ?= null,
    val response: List<Fight>,
    val errors: Any? = null)




data class Parameters(
    val season: String,
    val fighter: String

)

data class Fight(
    val id: Int,
    val date: String,
    val time: String,
    val timestamp: Long,
    val timezone: String,
    val slug: String,
    val is_main: Boolean,
    val category: String,
    val status: FightStatus,
    val fighters: FightFighters
)
data class FightStatus(
    val long: String,
    val short: String
)

data class FightFighters(
    val first: FighterDetails,
    val second: FighterDetails
)


data class FighterDetails(
    val id: Int,
    val name: String,
    val logo: String,
    val winner: Boolean?
)

data class FightResultsResponse(
    val get: String,
    val parameters: Parameters,
    val errors: Any?,
    val results: Int,
    val response: List<FightResult>
)


data class FightResult(
    val fight: FightId,
    val won_type: String?,
    val round: Int,
    val minute: String?,
    val ko_type: String?,
    val target: String?,
    val sub_type: String?,
    val score: List<String>?
)

data class FightId(
    val id: Int
)
data class CompleteFight(
    val fight: Fight,
    val result: FightResult?
)
