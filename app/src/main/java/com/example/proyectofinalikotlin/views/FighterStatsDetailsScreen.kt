package com.example.proyectofinalikotlin.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.proyectofinalikotlin.viewmodels.FighterStatsViewModel

@Composable
fun FighterStatsDetailsScreen(viewModel: FighterStatsViewModel, fighterId: String) {
    LaunchedEffect(fighterId) {
        viewModel.getFighterStats(fighterId)
    }

    val stats by remember { derivedStateOf { viewModel.selectedFighterRecord } }

    Column {
        stats?.let {
            Text("🏆 Victorias: ${it.total.win}", color = Color.Green)
            Text("❌ Derrotas: ${it.total.loss}", color = Color.Red)
        } ?: Text("No hay estadísticas disponibles")
    }
}
