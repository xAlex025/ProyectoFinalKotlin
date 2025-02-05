package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.grid.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FighterDetailScreen(viewModel: FighterViewModel, fighterId: String, navController: NavController) {
    LaunchedEffect(fighterId) {
        viewModel.getFighterById(fighterId)
    }

    val fighter = viewModel.selectedFighter
    val error = viewModel.error
    val errorMessage = viewModel.errorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 🔥 ENCABEZADO PERSONALIZADO (Sin TopAppBar)
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

            Text(
                text = "Detalles del Luchador",
                color = Color.White,
                fontSize = 22.sp
            )
        }



        when {
            error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = errorMessage, fontSize = 20.sp, color = Color.Red)
                }
            }

            fighter == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
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
                    // 🔥 Imagen grande del luchador
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(fighter.photo)
                            .build(),
                        modifier = Modifier
                            .size(260.dp), // 📌 Imagen más grande
                        contentDescription = fighter.name
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 🔥 Nombre del luchador
                    Text(
                        text = fighter.name,
                        fontSize = 26.sp,
                        color = Color.White
                    )

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
                        item { StatBox(value = fighter.category ?: "No disponible", name = "CATEGORÍA") }
                        item { StatBox(value = fighter.nickname ?: "No disponible", name = "APODO") }
                        item { StatBox(value = fighter.height ?: "No disponible", name = "ALTURA") }
                        item { StatBox(value = fighter.weight ?: "No disponible", name = "PESO") }
                        item { StatBox(value = fighter.age?.toString() ?: "No disponible", name = "EDAD") }
                        item { StatBox(value = fighter.reach ?: "No disponible", name = "ALCANCE") }
                        item { StatBox(value = fighter.stance ?: "No disponible", name = "ESTILO DE PELEA") }
                        item { StatBox(value = fighter.team?.name ?: "No disponible", name = "EQUIPO") }
                     }
                    }
                }
            }
        }
    }


// 📌 ESTILO DEL BOX PARA ESTADÍSTICAS (Como la imagen de referencia)
@Composable
fun StatBox(value: String, name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray) // 🔥 Fondo oscuro
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
    }
}
