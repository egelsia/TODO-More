package com.egelsia.todomore.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.egelsia.todomore.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    currentRoute: String?
) {
    val title = when (currentRoute) {
        "user" -> "Your Stats"
        "todo/{id}" -> "TODO"
        else -> ""
    }
    when (currentRoute) {
        "main" -> {
            CenterAlignedTopAppBar(
                title = { Icon(painterResource(R.drawable.todomore), contentDescription = "App Icon", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(80.dp)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                        }
                    ) {
                        Icon(Icons.Rounded.Menu, "Menu")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("user")
                        }
                    ) {
                        Icon(Icons.Rounded.AccountCircle, "Account")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        }
        else -> {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back")
                    }
                }
            )
        }
    }
}