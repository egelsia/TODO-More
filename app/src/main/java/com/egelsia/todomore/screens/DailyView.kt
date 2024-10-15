package com.egelsia.todomore.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.egelsia.todomore.data.StateHolder
import com.egelsia.todomore.viewmodels.TODOViewModel
import com.egelsia.todomore.data.todo.TODOItem
import com.egelsia.todomore.data.todo.TODOStatus
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ListView(
    modifier: Modifier = Modifier,
    todoViewModel: TODOViewModel,
    navController: NavHostController,
    route: String?
) {
    when(route) {
        "today" -> {
            todoViewModel.getListOfToday()
        }
        "week" -> {
            todoViewModel.getListOfThisWeek()
        }
        "month" -> {
            todoViewModel.getListOfThisMonth()
        }
        else -> {
            todoViewModel.getListOrderedByCreatedDate()
        }
    }

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
            val todoItems by state.data.collectAsState(initial = emptyList())
            LazyColumn {
                items(todoItems, key = {it.id}) { todoItem ->
                    SwipeToDeleteItem(
                        modifier = Modifier,
                        item = todoItem,
                        onDelete = {
                            todoViewModel.deleteTODOItem(it)
                        },
                        onClick = {navController.navigate("todo/${todoItem.id}")} ) {
                        TODOListItem(
                            modifier = Modifier,
                            todoItem = todoItem,
                            todoViewModel = todoViewModel
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (todoItems.isEmpty()) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Checked",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxSize(0.25f)
                    )
                    Text("You have no tasks to do!")
                }
            }
        }
    }
}

@Composable
fun TODOListItem(modifier: Modifier = Modifier,
                todoItem: TODOItem,
                todoViewModel: TODOViewModel
) {
    ListItem(
        headlineContent = {
        Text(text = todoItem.title)
        },
        supportingContent = {
            Column {
                if(todoItem.description != "") {
                    Text(text = todoItem.description)
                    Spacer(modifier = Modifier.height(5.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Label,
                        contentDescription = "Tag",
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${if (todoItem.category != "") todoItem.category.uppercase() + " • " else ""}${
                            todoItem.priorityLevel.name.lowercase()
                                .replaceFirstChar(Char::uppercase)
                        } Priority",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        },
        leadingContent = {
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
        },
        overlineContent = {
            val overlineStr = listOfNotNull(
                todoItem.completionDate?.let { "COMPLETED AT ${it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))}" },
                todoItem.dueDate?.let { "DUE ${it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))}" }
            ).joinToString(" ")

            if (overlineStr.isNotEmpty()) {
                Text(overlineStr)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState) {
    val color = when (swipeDismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Color.Red
        else -> Color.Transparent
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ){
        Icon(
            Icons.Rounded.Delete,
            contentDescription = "Delete",
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteItem(
    modifier: Modifier,
    item: T,
    onDelete: (T) -> Unit,
    onClick: () -> Unit,
    animationDuration: Int = 200,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
       mutableStateOf(false)
    }
    val context = LocalContext.current
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when(value){
                SwipeToDismissBoxValue.EndToStart -> {
                    isRemoved = true
                    true
                }
                else -> return@rememberSwipeToDismissBoxState false
            }
        },
        positionalThreshold = { it * .5f },

    )

    LaunchedEffect(isRemoved) {
        if(isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
            Toast.makeText(context, "The todo is successfully deleted.", Toast.LENGTH_SHORT).show()
        }
    }
    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = { DeleteBackground(swipeDismissState = state) },
            content = {
                Box(modifier = Modifier.clickable(onClick = { onClick() })) {
                    content(item)
                } },
            enableDismissFromStartToEnd = false
        )

    }
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