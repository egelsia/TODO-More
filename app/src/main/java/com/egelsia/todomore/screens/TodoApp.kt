package com.egelsia.todomore.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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

    val navBackStackEntry = navController.currentBackStackEntryAsState()

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
        skipPartiallyExpanded = false
    )

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            showBottomSheet = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                when (navBackStackEntry.value?.destination?.route) {
                    "user" -> {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Your Stats")
                            },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back")
                                }
                            }
                        )
                    }
                    else -> {
                        TopBar(
                            navController = navController,
                        )
                    }
                }
            },
            bottomBar = {
                when (navBackStackEntry.value?.destination?.route) {
                    "user" -> {}
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
                    //modifier = Modifier.align(Alignment.BottomCenter)
                )
//            {
//                Popup(
//                    onDismissRequest = {
//                        it.dismiss()
//                    }
//                ) {
//                    Snackbar(it)
//                }
//            }
            }
        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("main") {
                    DailyView(todoViewModel = todoViewModel)
                }
                composable("user") {
                    UserView()
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