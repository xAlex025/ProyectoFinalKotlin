package com.example.proyectofinalikotlin.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.model.Fighter
import com.example.proyectofinal.model.FighterRecord
import com.example.proyectofinal.model.FighterRecordResponse
import com.example.proyectofinalikotlin.network.ApiService
import kotlinx.coroutines.launch

class FighterStatsViewModel : ViewModel() {

    var fighterList by mutableStateOf<List<Fighter>>(emptyList()) // 🔥 Lista de todos los luchadores
        private set

    var selectedFighterRecord by mutableStateOf<FighterRecord?>(null) // 🔥 Estadísticas del luchador seleccionado
        private set


    var error by mutableStateOf(false)
        private set



    init {
        getAllFighters()
    }

    fun getAllFighters() {
        viewModelScope.launch {
            try {
                val categoriesResponse = ApiService.instance.getAllCategories()
                val allFighters = mutableListOf<Fighter>()

                for (category in categoriesResponse.response) {
                    val response = ApiService.instance.getFightersByCategory(category)
                    allFighters.addAll(response.response ?: emptyList())
                }

                fighterList = allFighters
                error = false
            } catch (e: Exception) {
                error = true
                fighterList = emptyList()
            }
        }
    }

    // ✅ Cargar estadísticas de un luchador cuando se selecciona
    fun getFighterStats(id: String) {
        viewModelScope.launch {
            try {
                val response = ApiService.instance.getFighterStats(id)
                if (response.results > 0) {
                    selectedFighterRecord = response.response.firstOrNull()
                    error = false
                } else {
                    selectedFighterRecord = null
                    error = true
                }
            } catch (e: Exception) {
                error = true
                selectedFighterRecord = null
            }
        }
    }
}