package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.layout.ContentScale
import com.example.proyectofinalikotlin.viewmodels.FighterStatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FighterRecordScreen(viewModel: FighterStatsViewModel, fighterId: String, navController: NavController) {
    LaunchedEffect(fighterId) {
        viewModel.getFighterStats(fighterId)
    }

    val record = viewModel.selectedFighterRecord
    val error = viewModel.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Estadísticas", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        when {
            error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error al cargar datos", fontSize = 20.sp, color = Color.Red)
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
                        .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(record.fighter.photo)
                            .build(),
                        modifier = Modifier.size(220.dp),
                        contentScale = ContentScale.Crop,
                        contentDescription = record.fighter.name
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = record.fighter.name,
                        fontSize = 28.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    FighterStatsBarChart(
                        wins = record.total.win,
                        losses = record.total.loss,
                        draws = record.total.draw ?: 0
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item { StatCard(value = record.total.win.toString(), label = "VICTORIAS", Color(0xFF303030)) }
                        item { StatCard(value = record.total.loss.toString(), label = "DERROTAS", Color(0xFF404040)) }
                        item { StatCard(value = record.total.draw?.toString() ?: "0", label = "EMPATES", Color(0xFF505050)) }
                        item { StatCard(value = record.ko.win.toString(), label = "KO GANADOS", Color(0xFF606060)) }
                        item { StatCard(value = record.ko.loss.toString(), label = "KO PERDIDOS", Color(0xFF707070)) }
                        item { StatCard(value = record.sub.win.toString(), label = "SUMISIONES WIN", Color(0xFF808080)) }
                        item { StatCard(value = record.sub.loss.toString(), label = "SUMISIONES LOSE", Color(0xFF909090)) }
                    }
                }
            }
        }
    }
}

@Composable
fun FighterStatsBarChart(wins: Int, losses: Int, draws: Int) {
    val totalFights = wins + losses + draws
    val winPercentage = if (totalFights > 0) (wins.toFloat() / totalFights) else 0f
    val lossPercentage = if (totalFights > 0) (losses.toFloat() / totalFights) else 0f
    val drawPercentage = if (totalFights > 0) (draws.toFloat() / totalFights) else 0f

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Rendimiento del Luchador", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.fillMaxWidth().height(30.dp)) {
            val barWidth = size.width
            val winWidth = barWidth * winPercentage
            val lossWidth = barWidth * lossPercentage
            val drawWidth = barWidth * drawPercentage

            var startX = 0f

            drawRoundRect(Color(0xFFD32F2F), androidx.compose.ui.geometry.Offset(startX, 0f), androidx.compose.ui.geometry.Size(winWidth, size.height))
            startX += winWidth
            drawRoundRect(Color(0xFFBDBDBD), androidx.compose.ui.geometry.Offset(startX, 0f), androidx.compose.ui.geometry.Size(drawWidth, size.height))
            startX += drawWidth
            drawRoundRect(Color(0xFF757575), androidx.compose.ui.geometry.Offset(startX, 0f), androidx.compose.ui.geometry.Size(lossWidth, size.height))
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun StatCard(value: String, label: String, backgroundColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}
