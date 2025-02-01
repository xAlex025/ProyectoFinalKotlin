package com.example.proyectofinalikotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.model.Fighter
import com.example.proyectofinalikotlin.ui.theme.ProyectoFinalIKotlinTheme
import com.example.proyectofinalikotlin.viewmodel.CategoryViewModel
import com.example.proyectofinalikotlin.views.FighterItem
import com.example.proyectofinalikotlin.viewmodels.FighterViewModel
import com.example.proyectofinalikotlin.views.CategoryListScreen
import com.example.proyectofinalikotlin.views.FighterListScreen

class MainActivity : ComponentActivity() {

    val fighterViewModel by viewModels<FighterViewModel>()
    val categoryViewModel by viewModels<CategoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFinalIKotlinTheme {
            CategoryListScreen(categoryViewModel, onCategoryClick = { category -> fighterViewModel.getFighterByCategory(category) });
            }
            }
        }
    }



