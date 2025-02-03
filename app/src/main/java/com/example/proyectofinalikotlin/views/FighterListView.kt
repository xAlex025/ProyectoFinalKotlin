package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.model.Fighter
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FighterListScreen(viewModel: FighterViewModel, category: String, onBack: () -> Unit) {
    val fighterList = viewModel.fighterListResponse
    val error = viewModel.error

    // ✅ Llama a la API solo una vez cuando la pantalla se carga
    LaunchedEffect(category) {
        viewModel.getFighterByCategory(category)
    }

    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.Red,
        topBar = {
            TopAppBar(
                title = { Text("Luchadores de $category", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Error de conexión", fontSize = 20.sp, color = Color.Red)
                    }
                }
                fighterList == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Red)
                    }
                }
                fighterList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No hay peleadores disponibles", fontSize = 20.sp, color = Color.White)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(fighterList) { _, item ->
                            FighterItem(fighter = item)
                        }
                    }
                }
            }
        }
    }
}

