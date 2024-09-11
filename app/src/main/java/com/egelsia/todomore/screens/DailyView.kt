package com.egelsia.todomore.screens

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.egelsia.todomore.data.StateHolder
import com.egelsia.todomore.data.TODOViewModel
import com.egelsia.todomore.data.todo.PriorityLevel
import com.egelsia.todomore.data.todo.TODOItem
import com.egelsia.todomore.data.todo.TODOStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale

@Composable
fun DailyView(modifier: Modifier = Modifier, todoViewModel: TODOViewModel) {
    todoViewModel.getListOrderedByCreatedDate()
    val todoState by todoViewModel.todoState.collectAsState()
    LaunchedEffect(Unit) {
        todoViewModel.getListOrderedByCreatedDate()
    }
    when(val state = todoState) {
        is StateHolder.Loading -> {
            CircularProgressIndicator()
        }

        is StateHolder.Error -> {
            Text("Error: ${state.message}")
        }
        is StateHolder.Success -> {
            val todoItems by state.user.collectAsState(initial = emptyList())
            LazyColumn {
                items(todoItems) { todoItem ->
                    TODOListItem(
                        modifier = Modifier.clickable(onClick = {}),
                        todoItem = todoItem,
                        todoViewModel = todoViewModel
                    )
                    HorizontalDivider(thickness = 0.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun TODOListItem(modifier: Modifier = Modifier,
                todoItem: TODOItem,
                todoViewModel: TODOViewModel) {
    ListItem(
        headlineContent = {
        Text(text = todoItem.title)
        },
        supportingContent = {
            if(todoItem.description != "") {
                Column {
                    Text(text = todoItem.description)
                    Spacer(modifier = Modifier.height(5.dp))
                    Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                        Icon(imageVector = Icons.AutoMirrored.Rounded.Label, contentDescription = "Category", modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "${ todoItem.category.uppercase() } • ${todoItem.priorityLevel.name.lowercase().replaceFirstChar(Char::uppercase) } Priority", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Light)
                    }
                }

            }
        },
        leadingContent = {
            TODOCheckbox(
                todoStatus = todoItem.status,
                onStatusChange = {todoStatus: TODOStatus ->
                    todoViewModel.upsertTODOItem(todoItem.copy(status = todoStatus))
                }
            )
        },
        overlineContent = {
            if (todoItem.dueDate != null) {
                Text(text = "DUE ${todoItem.dueDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))}")
            }
        },
    )
}

@Composable
fun TODOCheckbox(
    todoStatus: TODOStatus,
    onStatusChange: (TODOStatus) -> Unit
) {
    val state = when (todoStatus) {
        TODOStatus.TODO -> ToggleableState.Off
        TODOStatus.PROGRESS -> ToggleableState.Indeterminate
        TODOStatus.COMPLETED -> ToggleableState.On
    }

    TriStateCheckbox(
        state = state,
        onClick = {
            val newStatus = when (todoStatus) {
                TODOStatus.TODO -> TODOStatus.PROGRESS
                TODOStatus.PROGRESS -> TODOStatus.COMPLETED
                TODOStatus.COMPLETED -> TODOStatus.TODO
            }
            onStatusChange(newStatus)
        }
    )
}