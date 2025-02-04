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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.proyectofinalikotlin.ui.theme.ProyectoFinalIKotlinTheme
import com.example.proyectofinalikotlin.viewmodel.CategoryViewModel
import com.example.proyectofinalikotlin.viewmodels.FighterStatsViewModel
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel
import com.example.proyectofinalikotlin.views.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFinalIKotlinTheme {
                val navController = rememberNavController()
                val categoryViewModel: CategoryViewModel = viewModel()
                val fighterViewModel: FighterViewModel = viewModel()
                val fighterStatsViewModel: FighterStatsViewModel = viewModel()

                Scaffold(
                    topBar = { TopAppBarWithSearch(navController) },
                    bottomBar = { BottomNavigationBar(navController) }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues) // ✅ Respeta solo el padding inferior
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
                                FighterSearchScreen(viewModel = fighterViewModel)
                            }

                            composable("statistics") {
                                FighterStatsScreen(viewModel = fighterStatsViewModel) { fighterId ->
                                    navController.navigate("statistics/$fighterId")
                                }
                            }

                            composable("statistics/{fighterId}") { backStackEntry ->
                                val fighterId = backStackEntry.arguments?.getString("fighterId") ?: ""
                                FighterStatsDetailsScreen(viewModel = fighterStatsViewModel, fighterId = fighterId)
                            }

                            composable("fighter_detail/{fighterId}") { backStackEntry ->
                                val fighterId = backStackEntry.arguments?.getString("fighterId") ?: ""
                                FighterDetailScreen(
                                    viewModel = fighterViewModel,
                                    fighterId = fighterId,
                                    navController  = navController

                                )
                        }
                    }
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
                /* // 🔥 LOGO IZQUIERDA
                IconButton(
                    onClick = { /* Acción del logo */ },
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)

                    )
                }

                 */

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

                // 🔥 BOTÓN CIRCULAR DE BÚSQUEDA (LUPA) A LA DERECHA
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
        BottomNavItem("fight_search", "Peleas", Icons.Default.Face),
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


data class BottomNavItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
