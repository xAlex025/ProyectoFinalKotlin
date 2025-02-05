package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.proyectofinalikotlin.viewmodels.FighterStatsViewModel
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FighterRecordScreen(viewModel: FighterStatsViewModel, fighterId: String, navController: NavController) {
    LaunchedEffect(fighterId) {
        viewModel.getFighterStats(fighterId)
    }

    val record = viewModel.selectedFighterRecord
    val error = viewModel.error
    val errorMessage = viewModel.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 🔥 ENCABEZADO PERSONALIZADO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Récord del Luchador", color = Color.White, fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                   // Text(text = errorMessage, fontSize = 20.sp, color = Color.Red)
                }
            }
            record == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 🔥 Imagen del luchador
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(record.fighter.photo)
                            .build(),
                        modifier = Modifier.size(200.dp),
                        contentDescription = record.fighter.name
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔥 Nombre del luchador
                    Text(text = record.fighter.name, fontSize = 26.sp, color = Color.White)

                    Divider(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth(),
                        color = Color.Gray
                    )

                    // 📌 INFO EN GRID (2 COLUMNAS)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item { StatBox_2(value = record.total.win.toString(), label = "VICTORIAS") }
                        item { StatBox_2(value = record.total.loss.toString(), label = "DERROTAS") }
                        item { StatBox_2(value = record.total.draw?.toString() ?: "0", label = "EMPATES") }
                        item { StatBox_2(value = record.ko.win.toString(), label = "KO GANADOS") }
                        item { StatBox_2(value = record.ko.loss.toString(), label = "KO PERDIDOS") }
                        item { StatBox_2(value = record.sub.win.toString(), label = "SUMISIONES GANADAS") }
                        item { StatBox_2(value = record.sub.loss.toString(), label = "SUMISIONES PERDIDAS") }
                    }
                }
            }
        }
    }
}

// 📌 Componente reutilizable para mostrar estadísticas con formato mejorado
@Composable
fun StatBox_2(value: String, label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}
