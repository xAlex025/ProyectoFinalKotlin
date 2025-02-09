package com.example.proyectofinalikotlin.models


data class FightStatisticsResponse(
    val get: String,
    val parameters: Parameters,
    val errors: Any?,
    val results: Int,
    val response: List<FightStatistic>
)


data class FightStatistic(
    val fight: FightId,
    val fighter: FighterDetails,
    val strikes: Strikes
)


data class FighterId(
    val id: Int
)

data class Strikes(
    val total: StrikeDetails,
    val power: StrikeDetails,
    val takedowns: TakedownDetails,
    val submissions: Int,
    val control_time: String,
    val knockdowns: Int
)

data class StrikeDetails(
    val head: Int,
    val body: Int,
    val legs: Int
)

data class TakedownDetails(
    val attempt: Int,
    val landed: Int
)
