package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.proyectofinalikotlin.models.CompleteFight
import com.example.proyectofinalikotlin.models.FighterDetails
import com.example.proyectofinalikotlin.viewmodels.FightViewModel

@Composable
fun FightScreen(viewModel: FightViewModel, fighterId: String, season: String, navController: NavController) {
    LaunchedEffect(fighterId, season) {
        viewModel.getCompleteFighterFights(fighterId, season)
    }

    val fights by remember { derivedStateOf { viewModel.completeFighterFightsResponse } }
    val error by remember { derivedStateOf { viewModel.error } }
    val errorMessage by remember { derivedStateOf { viewModel.errorMessage } }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("fights") }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Peleas", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        when {
            error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage, fontSize = 20.sp, color = Color.Red)
                }
            }
            fights.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(fights) { completeFight ->
                        FightItem(completeFight, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun FightItem(completeFight: CompleteFight, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { navController.navigate("fight_stats/${completeFight.fight.id} ") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = completeFight.fight.slug.uppercase(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            val resultText = if (completeFight.result != null) {
                "${completeFight.result.won_type ?: "SIN RESULTADO"} - ASALTO ${completeFight.result.round}"
            } else {
                "SIN RESULTADO"
            }

            Text(
                text = resultText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FighterColumn(fighter = completeFight.fight.fighters.first, isWinner = completeFight.fight.fighters.first.winner == true)
                Text(
                    text = "VS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                FighterColumn(fighter = completeFight.fight.fighters.second, isWinner = completeFight.fight.fighters.second.winner == true)
            }
        }
    }
}

@Composable
fun FighterColumn(fighter: FighterDetails, isWinner: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(Color.Black, shape = RoundedCornerShape(50))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fighter.logo)
                    .crossfade(true)
                    .build(),
                contentDescription = fighter.name,
                modifier = Modifier
                    .size(90.dp)
                    .border(2.dp, Color.Red, CircleShape)
                    .background(Color.Black, shape = RoundedCornerShape(50))
                    .padding(4.dp)
            )

            if (isWinner) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(Color.Red, shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "GANADOR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = fighter.name.uppercase(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
