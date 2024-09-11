package com.egelsia.todomore.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.egelsia.todomore.R
import com.egelsia.todomore.components.AddTODOSheet
import com.egelsia.todomore.components.NavBar
import com.egelsia.todomore.components.TopBar
import com.egelsia.todomore.data.TODOViewModel
import com.egelsia.todomore.data.User
import com.egelsia.todomore.data.UserDao
import com.egelsia.todomore.data.UserViewModel
import com.egelsia.todomore.data.todo.TODOItemDao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(startDestination: String = "main", todoItemDao: TODOItemDao, userDao: UserDao) {

    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(2) }
    var showBottomSheet by remember{ mutableStateOf(false) }
    val todoViewModel = TODOViewModel(todoItemDao)
    val userViewModel = UserViewModel(userDao)
    var showUserNameDialog by remember {mutableStateOf(false)}

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    LaunchedEffect(Unit) {
        showUserNameDialog = userDao.getUser().isEmpty()
    }

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            showBottomSheet = false
        }
    }

    Scaffold(
        topBar = { TopBar(navController = navController, todoViewModel = todoViewModel) },
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
                DailyView(todoViewModel = todoViewModel)
            }
        }
    }

    if (showUserNameDialog) {
        UsernameDialog(
            onDismissRequest = { user -> userViewModel.upsertUser(user)
                               showUserNameDialog = false},
            onConfirmation = { user -> userViewModel.upsertUser(user)
                showUserNameDialog = false},
        )
    }

    if (showBottomSheet) {
        AddTODOSheet(
            onChange = { showBottomSheet = it }
        )
    }
}

@Preview
@Composable
fun UsernameDialog(
    onDismissRequest: (User) -> Unit = {},
    onConfirmation: (User) -> Unit = {},
) {
    var userName by remember { mutableStateOf("") }

    AlertDialog(
        icon = {Icon(painter = painterResource(R.drawable.todomore), contentDescription = "TO DO MORE!", modifier = Modifier.size(75.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)},
        title = {
            Text(text = "Welcome to To Do More. Let's meet!")
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "What would you like to be called?")
                Spacer(modifier = Modifier.height(10.dp))
                TextField(value = userName, onValueChange = {userName = it}, modifier = Modifier.fillMaxWidth())
            }

        },
        onDismissRequest = {
            onDismissRequest(User())
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val user = User(
                        userName = userName
                    )
                    onConfirmation(user)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest(User())
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}