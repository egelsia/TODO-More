package com.egelsia.todomore.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.egelsia.todomore.R
import com.egelsia.todomore.components.AddTODOSheet
import com.egelsia.todomore.components.NavBar
import com.egelsia.todomore.components.TopBar
import com.egelsia.todomore.viewmodels.TODOViewModel
import com.egelsia.todomore.data.todo.TODOItemDao
import com.egelsia.todomore.data.user.UserPreferences
import com.egelsia.todomore.viewmodels.SnackbarViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(startDestination: String = "main", todoItemDao: TODOItemDao) {

    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val todoViewModel = TODOViewModel(todoItemDao, userPreferences)
    val snackbarViewModel: SnackbarViewModel = viewModel()

    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(2) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showUserNameDialog by remember { mutableStateOf(!userPreferences.userExists()) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarViewModel.snackbarMessage.value) {
        snackbarViewModel.snackbarMessage.value?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message,
                    duration = snackbarViewModel.snackbarDuration.value
                )
                snackbarViewModel.clearSnackbar()
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            showBottomSheet = false
        }
    }


    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                currentRoute = currentRoute
            )
        },
        bottomBar = {
            when(currentRoute) {
                "todo/{id}" -> {}
                else -> {
                    NavBar(
                        selectedItem = selectedItem,
                        navController = navController,
                        onItemSelected = { selectedItem = it })
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            composable("main" ) {
                Box(modifier = Modifier.padding(paddingValues)){
                    ListView(todoViewModel = todoViewModel, navController = navController, route = currentRoute)
                }
            }
            composable("today" ) {
                Box(modifier = Modifier.padding(paddingValues)){
                    ListView(todoViewModel = todoViewModel, navController = navController, route = currentRoute)
                }
            }
            composable("week" ) {
                Box(modifier = Modifier.padding(paddingValues)){
                    ListView(todoViewModel = todoViewModel, navController = navController, route = currentRoute)
                }
            }
            composable("month" ) {
                Box(modifier = Modifier.padding(paddingValues)){
                    ListView(todoViewModel = todoViewModel, navController = navController, route = currentRoute)
                }
            }
            composable("user") {
                Box(modifier = Modifier.padding(paddingValues)){
                    UserView()
                }
            }
            composable("timer") {
                Box(modifier = Modifier.padding(paddingValues), contentAlignment = Alignment.Center){
                    Text("Not implemented yet.")
                }
            }
            composable("todo/{id}") { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString("id")?.toInt()
                Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding().value.dp)) {
                    TodoView(
                        id = id ?: -1,
                        todoViewModel = todoViewModel,
                        navController = navController,
                        snackbarViewModel = snackbarViewModel)
                }

            }
        }
    }

        if (showUserNameDialog) {
            UsernameDialog(
                onDismissRequest = { 
                    userPreferences.createUser("User")
                    showUserNameDialog = false
                },
                onConfirmation = { userName ->
                    userPreferences.createUser(userName)
                    showUserNameDialog = false
                },
            )
        }

        if (showBottomSheet) {
            AddTODOSheet(
                onChange = { showBottomSheet = it },
                todoViewModel = todoViewModel,
                snackbarViewModel = snackbarViewModel
            )
        }


}

@Preview
@Composable
fun UsernameDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: (String) -> Unit = {},
) {
    var userName by remember { mutableStateOf("") }

    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(R.drawable.todomore),
                contentDescription = "TO DO MORE!",
                modifier = Modifier.size(75.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        title = {
            Text(text = "Welcome to To Do More. Let's meet!")
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "What would you like to be called?")
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = userName,
                    onValueChange = { userName = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(userName)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}