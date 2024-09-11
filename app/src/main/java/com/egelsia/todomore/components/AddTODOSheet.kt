package com.egelsia.todomore.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egelsia.todomore.data.todo.PriorityLevel
import com.egelsia.todomore.data.todo.TODOStatus
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTODOSheet(
    onChange: (Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = { onChange(false) }
    ) {
        AddTodoItem(
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddTodoItem(modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf("")}
    var description by remember { mutableStateOf("")}
    var category by remember { mutableStateOf("")}
    var dueDate by remember { mutableStateOf(LocalDate.now()) }
    var status by remember { mutableStateOf(TODOStatus.TODO) }
    val reminder by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    var priorityLevel by remember {mutableStateOf(PriorityLevel.LOW)}
    var selectedIndex by remember { mutableIntStateOf(0) }
    var dropDownExpanded by remember { mutableStateOf(false)}
    val dropDownItems = listOf(
        PriorityLevel.LOW to "Low Priority",
        PriorityLevel.NORMAL to "Normal Priority",
        PriorityLevel.HIGH to "High Priority"
        )

    Column(modifier = modifier
        .fillMaxSize()
        .padding(20.dp)
        .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Title")},
            value = title,
            onValueChange = {title = it},

        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Description")},
            value = description,
            onValueChange = {description = it},
            )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Category")},
            value = category,
            onValueChange = {category = it},
        )
        Spacer(modifier = Modifier.height(10.dp))
        ExposedDropdownMenuBox(
            expanded = dropDownExpanded,
            onExpandedChange = {dropDownExpanded = !dropDownExpanded},
            modifier = Modifier
        ) {
            TextField(
                readOnly = true,
                label = {Text("Priority")},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = dropDownItems[selectedIndex].second,
                onValueChange = {},
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded)}
            )

            DropdownMenu(
                expanded = dropDownExpanded,
                onDismissRequest = { dropDownExpanded = false }) {
                dropDownItems.forEachIndexed { index, (level, string) ->
                    DropdownMenuItem(
                        text = { Text(text = string) },
                        onClick = {
                            selectedIndex = index
                            dropDownExpanded = false
                            priorityLevel = level
                        })
                }
            }
        }




    }
}