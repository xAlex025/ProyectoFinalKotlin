package com.example.proyectofinalikotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.proyectofinalikotlin.ui.theme.ProyectoFinalIKotlinTheme
import com.example.proyectofinalikotlin.viewmodel.CategoryViewModel
import com.example.proyectofinalikotlin.viewmodels.FightStatisticsViewModel
import com.example.proyectofinalikotlin.viewmodels.FightViewModel
import com.example.proyectofinalikotlin.viewmodels.FighterStatsViewModel
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel
import com.example.proyectofinalikotlin.views.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFinalIKotlinTheme {
                Main()
        }
    }
}


    @Composable
    fun Main() {


        val navController = rememberNavController()
        val categoryViewModel: CategoryViewModel = viewModel()
        val fighterViewModel: FighterViewModel = viewModel()
        val fighterStatsViewModel: FighterStatsViewModel = viewModel()
        val fightViewModel: FightViewModel = viewModel()
        val fightStatsViewModel: FightStatisticsViewModel = viewModel()

        Scaffold(
            topBar = { TopAppBarWithSearch(navController) },
            bottomBar = { BottomNavigationBar(navController) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "categories",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("categories") {
                        CategoryListScreen(
                            viewModel = categoryViewModel,
                            onCategoryClick = { category ->
                                fighterViewModel.getFighterByCategory(category)
                                navController.navigate("fighters/$category")
                            }
                        )
                    }

                    composable("fighters/{category}") { backStackEntry ->
                        val category = backStackEntry.arguments?.getString("category") ?: ""
                        FighterListScreen(
                            viewModel = fighterViewModel,
                            category = category,
                            onBack = { navController.popBackStack() },
                            navController = navController
                        )
                    }

                    composable("fight_search") {
                        FighterSearchScreen(viewModel = fighterViewModel, navController = navController)
                    }

                    composable("fighter_fights/{fighter.id}/year") {
                        FightScreen(viewModel =  fightViewModel , navController = navController, fighterId = "fighter.id", season = "year")
                    }

                    composable("fights") {
                        SelectFighterFightScreen(fighterViewModel = fighterViewModel, fighterStatsViewModel = fighterStatsViewModel, navController = navController)
                    }

                    composable("statistics") {
                        FighterStatsScreen(viewModel = fighterStatsViewModel, navController = navController)
                    }

                    composable("fighter_fights/{fighterId}/{year}") { backStackEntry ->
                        val fighterId = backStackEntry.arguments?.getString("fighterId")
                        val year = backStackEntry.arguments?.getString("year")

                        if (fighterId != null && year != null) {
                            FightScreen(fighterId = fighterId, season = year, viewModel = fightViewModel, navController = navController)
                        }
                    }


                    composable("fighter_detail/{fighterId}") { backStackEntry ->
                        val fighterId = backStackEntry.arguments?.getString("fighterId") ?: ""
                        FighterDetailScreen(
                            viewModel = fighterViewModel,
                            fighterId = fighterId,
                            navController  = navController

                        )
                    }
                    composable("fighter_stats_detail/{fighterId}") { backStackEntry ->
                        val fighterId = backStackEntry.arguments?.getString("fighterId") ?: ""
                        FighterRecordScreen(
                            viewModel = fighterStatsViewModel,
                            fighterId = fighterId,
                            navController  = navController

                        )
                    }

                    composable("fight_stats/{fightId}") { backStackEntry ->
                        val fightId = backStackEntry.arguments?.getString("fightId") ?: ""
                        FightStatsScreen(viewModel = fightStatsViewModel, fightId = fightId, navController = navController)
                    }

                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithSearch(navController: NavHostController) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Red),
        modifier = Modifier.fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
           ,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://pngimg.com/d/ufc_PNG61.png")
                       .build(),
                    modifier = Modifier
                        .clickable { navController.navigate("categories") }
                        .size(70.dp)
                        .padding(2.dp),
                    contentDescription = "Foto del luchador",


                )

                IconButton(
                    onClick = { navController.navigate("fight_search") },
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar Peleas",
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("categories", "Categorías", Icons.Default.List),
        BottomNavItem("fights", "Peleas", Icons.Filled.SportsMma),
        BottomNavItem("statistics", "Estadísticas", Icons.Default.Star)
    )

    NavigationBar(containerColor = Color.Black) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title, color = if (currentRoute == item.route) Color.Red else Color.White) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Red,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}}


data class BottomNavItem(val route: String, val title: String, val icon: ImageVector)

