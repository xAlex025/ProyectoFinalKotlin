package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.proyectofinalikotlin.viewmodels.FighterStatsViewModel
import com.example.proyectofinal.model.Fighter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FighterStatsScreen(viewModel: FighterStatsViewModel , onFighterClick: (String) -> Unit) {
    val fighterList by remember { derivedStateOf { viewModel.fighterList } }
    val selectedFighterRecord by remember { derivedStateOf { viewModel.selectedFighterRecord } }
    val error by remember { derivedStateOf { viewModel.error } }

    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.White,
        topBar = {
            TopAppBar(title = { Text("Estadísticas de Luchadores", color = Color.White) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (error) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error al cargar los datos", fontSize = 20.sp, color = Color.Red)
                }
            } else if (fighterList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            } else {
                // 🔥 Lista de luchadores
                LazyColumn(
                    modifier = Modifier.weight(1f), // ✅ Ocupa solo la mitad superior
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(fighterList) { fighter ->
                        FighterItem(fighter = fighter, onClick = { viewModel.getFighterStats(fighter.id.toString()) })
                    }
                }

                // 🔥 Estadísticas del luchador seleccionado
                selectedFighterRecord?.let { fighterStats ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = fighterStats.fighter.name, fontSize = 24.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("🏆 Victorias: ${fighterStats.total.win}", fontSize = 18.sp, color = Color.Green)
                            Text("❌ Derrotas: ${fighterStats.total.loss}", fontSize = 18.sp, color = Color.Red)
                            Text("➖ Empates: ${fighterStats.total.draw}", fontSize = 18.sp, color = Color.Gray)

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("🥊 KO: ${fighterStats.ko.win} - ${fighterStats.ko.loss}", fontSize = 18.sp, color = Color.Yellow)
                            Text("🤼‍♂️ Sumisiones: ${fighterStats.sub.win} - ${fighterStats.sub.loss}", fontSize = 18.sp, color = Color.Cyan)
                        }
                    }
                }
            }
        }
    }
}

/*
@Composable
fun FighterItem(fighter: Fighter, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔥 Imagen del luchador con borde rojo
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fighter.photo)
                    .transformations(CircleCropTransformation())
                    .build(),
                modifier = Modifier
                    .size(80.dp)
                    .border(2.dp, Color.Red, CircleShape)
                    .padding(2.dp),
                contentDescription = fighter.name
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 🔥 Información del luchador
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fighter.name.uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 🔥 Categoría con efecto degradado
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Red, Color.Black)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = fighter.category ?: "Desconocido",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        text = "Altura: ${fighter.height ?: "No disponible"}",
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Peso: ${fighter.weight ?: "No disponible"}",
                        fontSize = 14.sp,
                        color = Color.LightGray)
                }

            }
        }
    }
}

 */

