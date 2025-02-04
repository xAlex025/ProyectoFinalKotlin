package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavController
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
        // 🔥 ENCABEZADO PERSONALIZADO (En lugar de TopAppBar)
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

       // Spacer(modifier = Modifier.height(5.dp))

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
                          //  .padding(8.dp),
                        contentDescription = fighter.name
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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

                    // 🔥 Información detallada
                    InfoRow("Categoría", fighter.category ?: "No disponible")
                    InfoRow("Altura", fighter.height ?: "No disponible")
                    InfoRow("Peso", fighter.weight ?: "No disponible")
                    InfoRow("Edad", fighter.age?.toString() ?: "No disponible")
                    InfoRow("Alcance", fighter.reach ?: "No disponible")
                    InfoRow("Estilo de pelea", fighter.stance ?: "No disponible")
                    InfoRow("Equipo", fighter.team?.name ?: "No disponible")
                   }
            }
        }
    }
}

// 📌 Componente reutilizable para mostrar cada dato del luchador
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 18.sp)
        Text(text = value, color = Color.White, fontSize = 18.sp)
    }
}
