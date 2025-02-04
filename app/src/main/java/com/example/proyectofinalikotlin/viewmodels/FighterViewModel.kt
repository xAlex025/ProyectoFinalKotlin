package com.example.proyectofinalikotlin.viewmodels

    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.setValue
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.proyectofinal.model.Fighter
    import com.example.proyectofinalikotlin.network.ApiService
    import kotlinx.coroutines.launch

    class FighterViewModel() : ViewModel() {

        var fighterListResponse: List<Fighter> by mutableStateOf(listOf())
        var errorMessage: String by mutableStateOf("")
        var error: Boolean by mutableStateOf(false)
        var selectedFighter by mutableStateOf<Fighter?>(null)



        fun getFighterById(id: String) {
            viewModelScope.launch {
                try {
                    val response = ApiService.instance.getFightersById(id)

                    if (response.results > 0 && response.response.isNotEmpty()) {
                        selectedFighter = response.response.first() // ✅ Solo un luchador
                        error = false
                    } else {
                        errorMessage = "No se encontró ningún luchador con ID: $id"
                        error = true
                        selectedFighter = null
                    }
                } catch (e: Exception) {
                    errorMessage = "Error al obtener los datos: ${e.message}"
                    error = true
                    selectedFighter = null
                }
            }
        }


            fun getFighterByCategory(category: String) {
            viewModelScope.launch {
                try {

                    fighterListResponse = emptyList()
                    val response = ApiService.instance.getFightersByCategory(category)
                    if (response.results > 0) {
                        fighterListResponse = response.response // ✅ Asigna la lista de luchadores
                        error = false
                    } else {
                        errorMessage = "No se encontró ningún luchador con ID: $category"
                        error = true
                    }
                } catch (e: Exception) {
                    errorMessage = "Error al obtener los datos: ${e.message}"
                    error = true
                }
            }
        }

/*
        fun getFighterBySearch(search: String) {
            viewModelScope.launch {
                try {
                    val response = ApiService.instance.getFightersBySearch(search)
                    if (response.results > 0) {
                        fighterListResponse = response.response // ✅ Asigna la lista de luchadores
                        error = false
                    } else {
                        errorMessage = "No se encontró ningún luchador con el nombre: $search"
                        error = true
                    }
                } catch (e: Exception) {
                    errorMessage = "Error al obtener los datos: ${e.message}"
                    error = true
                }
            }
        }


 */

        fun getFighterBySearch(search: String, callback: (List<Fighter>) -> Unit) {
            viewModelScope.launch {
                try {
                    val response = ApiService.instance.getFightersBySearch(search)
                    if (response.results > 0) {
                        callback(response.response) // 🔥 Devuelve solo los resultados filtrados
                    } else {
                        callback(emptyList()) // 🔥 Devuelve lista vacía si no hay resultados
                    }
                } catch (e: Exception) {
                    callback(emptyList()) // 🔥 En caso de error, devuelve lista vacía
                }
            }
        }

/*
        fun getFighterBySearch(search: String): List<Fighter> {
            return fighterListResponse.filter { it.name.contains(search, ignoreCase = true) }
        }

 */

        fun resetFighterList() {
            fighterListResponse = emptyList()
            error = false
        }


        fun getAllFighters() {
            viewModelScope.launch {
                try {
                    val categoriesResponse = ApiService.instance.getAllCategories() // 🔥 Llamada a la API de categorías
                    val allFighters = mutableListOf<Fighter>()

                    for (category in categoriesResponse.response) {
                        val response = ApiService.instance.getFightersByCategory(category)
                        allFighters.addAll(response.response ?: emptyList()) // 🔥 Se añaden luchadores de cada categoría
                    }

                    fighterListResponse = allFighters
                    error = false
                } catch (e: Exception) {
                    error = true
                    fighterListResponse = emptyList()
                }
            }
        }




        fun getErrorCon(): Boolean = error
    }
