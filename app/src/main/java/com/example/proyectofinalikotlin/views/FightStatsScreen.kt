package com.example.proyectofinalikotlin.views

import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectofinal.model.Fighter
import com.example.proyectofinalikotlin.models.FightResult
import com.example.proyectofinalikotlin.models.FightStatisticsResponse
import com.example.proyectofinalikotlin.viewmodels.FightStatisticsViewModel

@Composable
fun FightStatsScreen(
    viewModel: FightStatisticsViewModel,
    fightId: String,
    navController: NavController
) {
    LaunchedEffect(fightId) {
        viewModel.resetState()
        viewModel.getFightStatistics(fightId)
    }

    val fightStats by remember { derivedStateOf { viewModel.fightStatisticsResponse } }
    val fightResult by remember { derivedStateOf { viewModel.fightResult } }
    val fighterOne by remember { derivedStateOf { viewModel.fighterOne } }
    val fighterTwo by remember { derivedStateOf { viewModel.fighterTwo } }
    val error by remember { derivedStateOf { viewModel.error } }
    val errorMessage by remember { derivedStateOf { viewModel.errorMessage } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Estadísticas de la Pelea", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        when {
            error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage, fontSize = 20.sp, color = Color.Red)
                }
            }
            fightStats == null || fighterOne == null || fighterTwo == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }
            else -> {
                FightStatsContent(fightStats!!, fightResult, fighterOne!!, fighterTwo!!)
            }
        }
    }
}

@Composable
fun FightStatsContent(
    stats: FightStatisticsResponse,
    fightResult: FightResult?,
    fighterOne: Fighter,
    fighterTwo: Fighter
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FighterColumnStats(fighter = fighterOne)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Asalto", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${fightResult?.round ?: "-"}", fontSize = 35.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = fightResult?.won_type ?: "N/A", fontSize = 16.sp, color = Color.White)
                Text(text = fightResult?.minute ?: "N/A", fontSize = 14.sp, color = Color.Gray)
            }

            FighterColumnStats(fighter = fighterTwo)
        }

        Spacer(modifier = Modifier.height(20.dp))

        StatsRow("Derribos", stats.response[0].strikes.takedowns.landed.toString(), stats.response[1].strikes.takedowns.landed.toString())
        Spacer(modifier = Modifier.height(5.dp))
        StatsRow("Total Golpes", stats.response[0].strikes.total.head.toString(), stats.response[1].strikes.total.head.toString())
        Spacer(modifier = Modifier.height(5.dp))
        StatsRow("Golpes Claves", stats.response[0].strikes.power.head.toString(), stats.response[1].strikes.power.head.toString())
        Spacer(modifier = Modifier.height(5.dp))
        StatsRow("Intentos de Sumisión", stats.response[0].strikes.submissions.toString(), stats.response[1].strikes.submissions.toString())
        Spacer(modifier = Modifier.height(5.dp))
        StatsRow("Tiempo de Control", stats.response[0].strikes.control_time, stats.response[1].strikes.control_time)
    }
}

@Composable
fun FighterColumnStats(fighter: Fighter) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = fighter.photo,
            contentDescription = fighter.name,
            modifier = Modifier
                .size(120.dp)
                .background(Color.Black, shape = RoundedCornerShape(50))
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = fighter.name?.uppercase() ?: "DESCONOCIDO",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun StatsRow(title: String, stat1: String, stat2: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Divider(color = Color.Red, thickness = 2.dp)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stat1, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(text = stat2, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
