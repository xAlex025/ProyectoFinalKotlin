    package com.example.proyectofinalikotlin.viewmodels

    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.setValue
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.proyectofinal.model.Fighter
    import com.example.proyectofinalikotlin.network.ApiService
    import kotlinx.coroutines.launch

    class FighterViewModel : ViewModel() {

        var fighterListResponse: List<Fighter> by mutableStateOf(listOf())
        var errorMessage: String by mutableStateOf("")
        var error: Boolean by mutableStateOf(false)

        fun getFighterById(id: String) {
            viewModelScope.launch {
                try {
                    val response = ApiService.instance.getFightersById(id) // ✅ Ahora sí devuelve datos

                    if (response.results > 0) {
                        fighterListResponse = response.response // ✅ Asigna la lista de luchadores
                        error = false
                    } else {
                        errorMessage = "No se encontró ningún luchador con ID: $id"
                        error = true
                    }
                } catch (e: Exception) {
                    errorMessage = "Error al obtener los datos: ${e.message}"
                    error = true
                }
            }
        }

            fun getFighterByCategory(category: String) {
            viewModelScope.launch {
                try {
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



        fun getErrorCon(): Boolean = error
    }
