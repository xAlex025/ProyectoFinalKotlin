package com.example.proyectofinalikotlin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalikotlin.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    var categories : List<String> by mutableStateOf(listOf());
    var errorMessage: String by mutableStateOf("")
    var error: Boolean by mutableStateOf(false)

    init {
        getAllCategories()
    }

    fun getAllCategories () {
        viewModelScope.launch {
            try {
                val response = ApiService.instance.getAllCategories() // ✅ Ahora sí devuelve datos

                if (response.results > 0) {
                    categories = response.response // ✅ Asigna la lista de luchadores
                    error = false
                } else {
                    errorMessage = "No se encontró  ninguna categoria"
                    error = true
                }
            } catch (e: Exception) {
                errorMessage = "Error al obtener los datos: ${e.message}"
                error = true
            }
        }
    }
}
