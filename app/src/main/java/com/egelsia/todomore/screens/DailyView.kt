package com.egelsia.todomore.screens

import android.content.res.Resources
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.egelsia.todomore.data.StateHolder
import com.egelsia.todomore.data.TODOViewModel
import com.egelsia.todomore.data.todo.PriorityLevel
import com.egelsia.todomore.data.todo.TODOItem
import com.egelsia.todomore.data.todo.TODOStatus
import java.util.Date

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
                    TODOListItem(todoItem = todoItem)
                    HorizontalDivider(thickness = 0.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Preview
@Composable
fun TODOListItem(modifier: Modifier = Modifier,
                 todoItem: TODOItem = TODOItem(
                     title = "TODO Title",
                     description = "TODO Description",
                     dueDate = Date(System.currentTimeMillis() + 264000),
                     category = "TODO Category",
                     priorityLevel = PriorityLevel.NORMAL,
                     status = TODOStatus.TODO
                 )) {
    Column (modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(10.dp)
    ){
        Box {
            Text(text = todoItem.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box {
            Text(text = todoItem.description, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            Icon(imageVector = Icons.AutoMirrored.Rounded.Label, contentDescription = "Category", modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = todoItem.category.uppercase(), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Light)
        }
    }


}