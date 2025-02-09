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
                        selectedFighter = response.response.first()
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
                        fighterListResponse = response.response
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


        fun getFighterBySearch(search: String, callback: (List<Fighter>) -> Unit) {
            viewModelScope.launch {
                try {
                    val response = ApiService.instance.getFightersBySearch(search)
                    if (response.results > 0) {
                        callback(response.response)
                    } else {
                        callback(emptyList())
                    }
                } catch (e: Exception) {
                    callback(emptyList())
                }
            }
        }


        fun resetFighterList() {
            fighterListResponse = emptyList()
            error = false
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
