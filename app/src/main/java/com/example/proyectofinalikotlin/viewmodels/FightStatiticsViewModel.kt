package com.example.proyectofinalikotlin.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.model.Fighter
import com.example.proyectofinalikotlin.models.*
import com.example.proyectofinalikotlin.network.ApiService
import kotlinx.coroutines.launch

class FightStatisticsViewModel : ViewModel() {

    var fightStatisticsResponse: FightStatisticsResponse? by mutableStateOf(null)
    var fightResult: FightResult? by mutableStateOf(null)
    var fighterOne: Fighter? by mutableStateOf(null)
    var fighterTwo: Fighter? by mutableStateOf(null)
    var errorMessage: String by mutableStateOf("")
    var error by mutableStateOf(false)

    fun resetState() {
        fightStatisticsResponse = null
        fightResult = null
        fighterOne = null
        fighterTwo = null
        error = false
        errorMessage = ""
    }

    fun getFightStatistics(fightId: String) {
        viewModelScope.launch {
            try {
                resetState()

                val statsResponse = ApiService.instance.getFightsStatitics(fightId.trim().toInt())
                val resultResponse = ApiService.instance.getFighterFightsResults(fightId.trim().toInt())

                println("📢 Estadísticas de la pelea: $statsResponse")
                println("📢 Resultado de la pelea: $resultResponse")

                fightStatisticsResponse = statsResponse
                fightResult = resultResponse.response?.firstOrNull()

                val fighterOneId = statsResponse.response[0].fighter.id
                val fighterTwoId = statsResponse.response[1].fighter.id

                val fighterOneResponse = ApiService.instance.getFightersById(fighterOneId.toString())
                val fighterTwoResponse = ApiService.instance.getFightersById(fighterTwoId.toString())

                fighterOne = fighterOneResponse.response?.firstOrNull()
                fighterTwo = fighterTwoResponse.response?.firstOrNull()

                println("✅ Peleador 1: $fighterOne")
                println("✅ Peleador 2: $fighterTwo")

            } catch (e: Exception) {
                errorMessage = "Error al obtener estadísticas: ${e.message}"
                error = true
                println("❌ Error en estadísticas: ${e.message}")
            }
        }
    }
}
