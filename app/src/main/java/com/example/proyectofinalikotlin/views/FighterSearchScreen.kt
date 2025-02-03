package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.model.Fighter
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel
@Composable
fun FighterSearchScreen(viewModel: FighterViewModel) {
    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(emptyList<Fighter>()) }
    var isSearching by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()// ✅ Respeta la TopAppBar

    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                if (query.isNotBlank()) {
                    isSearching = true
                    viewModel.getFighterBySearch(query) { results ->
                        searchResults = results
                        isSearching = false
                    }
                } else {
                    searchResults = emptyList() // ✅ Limpiar lista si el input está vacío
                }
            },
            label = { Text("Buscar Luchador") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isSearching -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            searchResults.isEmpty() && query.isNotBlank() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron resultados para \"$query\"")
                }
            }
            searchResults.isNotEmpty() -> {
                LazyColumn {
                    items(searchResults) { fighter ->
                        FighterItem(fighter)
                    }
                }
            }
        }
    }
}

