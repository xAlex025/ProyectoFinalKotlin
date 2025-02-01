package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinalikotlin.viewmodel.CategoryViewModel



@Composable
fun CategoryListScreen(viewModel: CategoryViewModel, onCategoryClick: (String) -> Unit) {
    val categories = viewModel.categories
    val error = viewModel.error
    val errorMessage = viewModel.errorMessage

    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.Red
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // ✅ Aplica el padding del Scaffold
        ) {
            when {
                error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = errorMessage, fontSize = 20.sp, color = Color.Red)
                    }
                }
                categories.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Red)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp) // 🔥 Espaciado entre elementos
                    ) {
                        items(categories) { category ->
                            CategoryItem(category, onCategoryClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: String, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // 🔥 Ahora ocupa todo el ancho disponible
            .padding(horizontal = 16.dp, vertical = 4.dp) // 🔥 Espaciado horizontal y vertical uniforme
            .clickable { onClick(category) },
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray,
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = category,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp),
            color = Color.White
        )
    }
}


