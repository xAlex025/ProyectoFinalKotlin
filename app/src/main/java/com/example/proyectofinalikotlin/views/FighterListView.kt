package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.proyectofinal.model.Fighter
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FighterListScreen(viewModel: FighterViewModel, category: String, onBack: () -> Unit, navController: NavHostController) {
    val fighterList = viewModel.fighterListResponse
    val error = viewModel.error

    LaunchedEffect(category) {
        viewModel.getFighterByCategory(category)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Luchadores de $category",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error de conexión", fontSize = 20.sp, color = Color.Red)
                    }
                }
                fighterList == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Red)
                    }
                }
                fighterList.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay peleadores disponibles", fontSize = 20.sp, color = Color.White)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(fighterList) { _, item ->
                            FighterItem(fighter = item, onClick = {
                                navController.navigate("fighter_detail/${item.id}")
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FighterItem(fighter: Fighter, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fighter.photo)
                    .transformations(CircleCropTransformation())
                    .build(),
                contentDescription = "Foto de ${fighter.name}",
                modifier = Modifier
                    .size(80.dp)
                    .border(2.dp, Color.Red, CircleShape)
                    .padding(2.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = fighter.name.uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

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
                        text = "Altura: ${fighter.height ?: "N/D"}",
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Peso: ${fighter.weight ?: "N/D"}",
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}
