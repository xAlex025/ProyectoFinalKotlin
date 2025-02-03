package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.proyectofinal.model.Fighter

@Composable
fun FighterItem(fighter: Fighter) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp), // 🔥 Bordes redondeados
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // 🔥 Sombra
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray) // 🔥 Fondo mejorado
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔥 Imagen del luchador con borde
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fighter.photo)
                    .transformations(CircleCropTransformation())
                    .build(),
                modifier = Modifier
                    .size(80.dp)
                    .border(2.dp, Color.Red, CircleShape) // 🔥 Borde rojo alrededor de la imagen
                    .padding(2.dp),
                contentDescription = fighter.name
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 🔥 Información del luchador
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = fighter.name.uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 🔥 Categoría con Chip estilizado
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Red, Color.DarkGray)
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

