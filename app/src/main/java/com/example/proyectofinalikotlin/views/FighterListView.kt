package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.model.Fighter
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel

@Composable
fun FighterListScreen(viewModel: FighterViewModel) {
    // ✅ Ahora la UI observa directamente los cambios en el ViewModel
    val fighterList by remember { derivedStateOf { viewModel.fighterListResponse } }
    val error by remember { derivedStateOf { viewModel.error } }

    // ✅ Llamar a la API en `init` dentro del `ViewModel`, no en la UI
    viewModel.getFighterByCategory("Flyweight")

    when {
        error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error de conexión", fontSize = 20.sp)
            }
        }
        fighterList == null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        fighterList!!.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No hay peleadores disponibles", fontSize = 20.sp)
            }
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(fighterList!!) { _, item ->
                    FighterItem(fighter = item)
                }
            }
        }
    }
}
