package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofinal.model.Fighter
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FighterSearchScreen(viewModel: FighterViewModel, navController: NavHostController) {
    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(emptyList<Fighter>()) }
    var isSearching by remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
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
                    searchResults = emptyList()
                }
            },
            label = { Text("Buscar Luchador", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray
            )
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
                        FighterItem(fighter, onClick = {
                            navController.navigate("fighter_detail/${fighter.id}")
                        })
                    }
                }
            }
        }
    }
}
