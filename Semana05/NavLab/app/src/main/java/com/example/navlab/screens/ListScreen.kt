package com.example.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.navlab.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    val elementos = remember {
        mutableStateListOf<Int>().apply { addAll(1..8) }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de elementos") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding
        ) {
            items(elementos, key = { it }) { numero ->
                ListItem(
                    headlineContent = { Text("Elemento número $numero") },
                    supportingContent = { Text("Toca para ver el detalle") },
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Detail.createRoute(numero))
                    },
                    trailingContent = {
                        Button(onClick = { elementos.remove(numero) }) {
                            Text("Eliminar")
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}