package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.proyectofinal.model.Fighter
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel
import com.example.proyectofinalikotlin.viewmodels.FighterStatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFighterFightScreen(
    fighterViewModel: FighterViewModel,
    fighterStatsViewModel: FighterStatsViewModel,
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFighter by remember { mutableStateOf<Fighter?>(null) }
    var selectedYear by remember { mutableStateOf<String?>(null) }
    var expandedYear by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fighterViewModel.getAllFighters()
    }

    val fighters by remember { derivedStateOf { fighterViewModel.fighterListResponse ?: emptyList() } }
    val years = (2022..2025).map { it.toString() }

    val filteredFighters = remember(searchQuery, fighters) {
        fighters.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Buscar Luchador y Año", color = Color.White, fontSize = 22.sp)
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar Luchador", color = Color.White) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredFighters.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .background(Color.DarkGray)
            ) {
                items(filteredFighters) { fighter ->
                    FighterListItem(
                        fighter = fighter,
                        isSelected = fighter == selectedFighter,
                        onSelect = { selectedFighter = fighter }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No hay resultados", color = Color.Gray, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { expandedYear = true }, modifier = Modifier.fillMaxWidth()) {
                Text(text = selectedYear ?: "Seleccionar Año")
            }
            DropdownMenu(expanded = expandedYear, onDismissRequest = { expandedYear = false }) {
                years.forEach { year ->
                    DropdownMenuItem(
                        text = { Text(year) },
                        onClick = {
                            selectedYear = year
                            expandedYear = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                selectedFighter?.let { fighter ->
                    selectedYear?.let { year ->
                        navController.navigate("fighter_fights/${fighter.id}/$year")
                    }
                }
            },
            enabled = selectedFighter != null && selectedYear != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ver Peleas")
        }
    }
}

@Composable
fun FighterListItem(fighter: Fighter, isSelected: Boolean, onSelect: (Fighter) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onSelect(fighter) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color.Red else Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fighter.photo)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .size(60.dp)
                    .border(2.dp, if (isSelected) Color.White else Color.Red, CircleShape)
                    .padding(2.dp)
                    .background(Color.Black, CircleShape),
                contentDescription = fighter.name
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
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
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
