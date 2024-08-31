package com.egelsia.todomore.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.egelsia.todomore.components.NavBar
import com.egelsia.todomore.components.TopBar

@Composable
fun NavController(startDestination: String = "main") {

    val navController = rememberNavController()

    var selectedItem by remember { mutableIntStateOf(2) }

    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = { NavBar(selectedItem = selectedItem, navController = navController, onItemSelected = { selectedItem = it })},
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("main") }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
            }
        },
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("main") {
                Text("TODOMORE")

            }
        }

    }
}