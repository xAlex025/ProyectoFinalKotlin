package com.example.proyectofinalikotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
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
                            .padding(
                                top = paddingValues.calculateTopPadding() - 41.dp, // 🔥 Reduce el espacio superior
                                bottom = paddingValues.calculateBottomPadding()
                            ) // ✅ Solo padding inferior
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
                                )
                            }

                            composable("fight_search") {
                                FighterSearchScreen(viewModel = fighterViewModel )
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
        modifier = Modifier.fillMaxWidth(), // ✅ Ocupa TODO el ancho de la pantalla
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(horizontal = 16.dp, vertical = 8.dp), // ✅ Evita desalineaciones
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 🔥 LOGO IZQUIERDA
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(Color.White, shape = CircleShape)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // 🔥 BOTÓN CIRCULAR DE BÚSQUEDA (LUPA) A LA DERECHA
                IconButton(
                    onClick = { navController.navigate("fight_search") },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar Peleas",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Red, // ✅ Asegura el fondo rojo visible
            titleContentColor = Color.White
        )
    )
}



@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("categories", "Categorías", Icons.Default.List),
        BottomNavItem("fight_search", "Peleas", Icons.Default.Face), // 🔥 Ahora tiene un icono mejor
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
}

data class BottomNavItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
