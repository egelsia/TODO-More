package com.egelsia.todomore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.egelsia.todomore.components.AddTODOSheet
import com.egelsia.todomore.data.StateHolder
import com.egelsia.todomore.data.todo.TODOItem
import com.egelsia.todomore.data.todo.TODOStatus
import com.egelsia.todomore.viewmodels.SnackbarViewModel
import com.egelsia.todomore.viewmodels.TODOViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun TodoView(
    modifier: Modifier = Modifier,
    id: Int,
    todoViewModel: TODOViewModel,
    navController: NavController,
    snackbarViewModel: SnackbarViewModel
) {
    todoViewModel.getTODOItemById(id)
    val todoItemAsState = todoViewModel.singleTodoState.collectAsState()
    var showTODOSheet by remember { mutableStateOf(false) }

    when(val state = todoItemAsState.value){
        is StateHolder.Error -> {
            Box(contentAlignment = Alignment.Center) {
                Text(state.message)
            }
        }
        StateHolder.Loading -> {
            CircularProgressIndicator()
        }
        is StateHolder.Success -> {
            val todoItem = state.data.collectAsState(TODOItem()).value
            var reminder by remember { mutableStateOf(todoItem.reminder)}
            Scaffold(
                bottomBar = {
                    BottomAppBar(
                        actions = {
                            IconButton(onClick = { showTODOSheet = true }) {
                                Icon(
                                    Icons.Rounded.Edit,
                                    contentDescription = "Save TO DO",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            TODOCheckbox(
                                todoStatus = todoItem.status,
                                onStatusChange = {todoStatus: TODOStatus ->
                                    if (todoStatus != TODOStatus.COMPLETED) {
                                        todoViewModel.upsertTODOItem(todoItem.copy(status = todoStatus, completionDate = null))
                                    } else {
                                        todoViewModel.upsertTODOItem(todoItem.copy(status = todoStatus, completionDate = LocalDate.now()))
                                    }
                                }
                            )
                            IconButton(onClick = {
                                reminder = !reminder
                                todoViewModel.upsertTODOItem(todoItem.copy(reminder = reminder))
                            }) {
                                Icon(
                                    imageVector =  if (reminder) Icons.Rounded.NotificationsActive else Icons.Rounded.NotificationsNone,
                                    contentDescription = "Save TO DO",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                    )
                }
            ) { paddingValues ->

                Box(modifier = Modifier
                    .padding(bottom = paddingValues.calculateBottomPadding().value.dp)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {

                        ListItem(
                            overlineContent = {
                                Text(text = "Title")
                            },
                            headlineContent = {
                                Text(text = todoItem.title)
                            }
                        )
                        HorizontalDivider()
                        ListItem(
                            overlineContent = {
                                Text("Description")
                            },
                            headlineContent = {
                                Text(
                                    text = todoItem.description,
                                )
                            }
                        )
                        HorizontalDivider()
                        ListItem(
                            overlineContent = { Text(text = "Category")},
                            headlineContent = {
                                Text(text = todoItem.category)
                            }
                        )

                        HorizontalDivider()
                        ListItem(
                            overlineContent = {
                                Text("Task Date")
                            },
                            headlineContent = {
                                Text(text = todoItem.createdDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))
                            }
                        )
                        HorizontalDivider()
                        ListItem(
                            overlineContent = {
                                Text("Due Date")
                            },
                            headlineContent = {
                                Text(text =
                                if(todoItem.dueDate == null) {
                                    "Due date is not specified."
                                } else {
                                    todoItem.dueDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                                }
                                )
                            }
                        )
                        HorizontalDivider()
                        ListItem(
                            overlineContent = {
                                Text("Completed At")
                            },
                            headlineContent = {
                                Text(text =
                                if (todoItem.completionDate == null) {
                                    "Task is not completed yet."
                                } else {
                                    todoItem.completionDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                                })

                            }
                        )
                    }
                }

                if (showTODOSheet) {
                    AddTODOSheet(
                        onChange = {showTODOSheet = false},
                        todoViewModel = todoViewModel,
                        todoItem = todoItem,
                        snackbarViewModel = snackbarViewModel
                    )
                }
            }
        }
    }
}
