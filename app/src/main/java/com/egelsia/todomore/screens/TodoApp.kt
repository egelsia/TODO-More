package com.egelsia.todomore.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.egelsia.todomore.components.AddTODOSheet
import com.egelsia.todomore.components.NavBar
import com.egelsia.todomore.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(startDestination: String = "main") {

    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(2) }
    var showBottomSheet by remember{ mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            showBottomSheet = false
        }
    }

    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = { NavBar(selectedItem = selectedItem, navController = navController, onItemSelected = { selectedItem = it })},
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
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
    if (showBottomSheet) {
        AddTODOSheet(
            onChange = { showBottomSheet = it }
        )
    }
}