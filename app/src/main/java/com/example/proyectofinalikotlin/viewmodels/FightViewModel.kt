package com.example.proyectofinalikotlin.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectofinalikotlin.models.Fight
import com.example.proyectofinalikotlin.network.ApiService
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalikotlin.models.CompleteFight
import kotlinx.coroutines.launch


class FightViewModel  : ViewModel(){


    var completeFighterFightsResponse by mutableStateOf<List<CompleteFight>>(emptyList())
    var fighterFights by mutableStateOf<List<Fight>>(emptyList())
    var errorMessage: String by mutableStateOf("")
    var error: Boolean by mutableStateOf(false)

    fun getFighterFights(fighterId: String, year: String) {
        viewModelScope.launch {
            try {
                val response = ApiService.instance.getFighterFights( fighterId,year)
                if (response.results!! > 0) {
                    // Filtrar  peleas por año
                    fighterFights = response.response
                    error = false
                } else {
                    errorMessage = "No hay peleas para el año $year"
                    error = true
                }
            } catch (e: Exception) {
                errorMessage = "Error al obtener los datos: ${e.message}"
                error = true
            }
        }
    }

    fun getCompleteFighterFights(fighterId: String, season: String) {
        viewModelScope.launch {
            try {
                completeFighterFightsResponse = emptyList()
                error = false
                errorMessage = ""


                val fightResponse = ApiService.instance.getFighterFights(fighterId, season)

                if ((fightResponse.results ?: 0) == 0 || fightResponse.response.isNullOrEmpty()) {
                    errorMessage = "No hay peleas registradas para este luchador en $season"
                    error = true
                    return@launch
                }

                val fightResults = fightResponse.response.mapNotNull { fight ->
                    val resultsResponse = ApiService.instance.getFighterFightsResults(fight.id)


                    resultsResponse.response?.firstOrNull()?.let { result ->
                        CompleteFight(fight = fight, result = result)
                    }
                }

                completeFighterFightsResponse = fightResults
                error = false

            } catch (e: Exception) {
                errorMessage = "Error al obtener las peleas del luchador: ${e.message}"
                error = true
                completeFighterFightsResponse = emptyList()
            }
        }
    }


}

