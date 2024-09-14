package com.egelsia.todomore.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egelsia.todomore.viewmodels.TODOViewModel
import com.egelsia.todomore.data.todo.PriorityLevel
import com.egelsia.todomore.data.todo.TODOItem
import com.egelsia.todomore.viewmodels.SnackbarViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTODOSheet(
    onChange: (Boolean) -> Unit,
    todoViewModel: TODOViewModel,
    todoItem: TODOItem? = null,
    snackbarViewModel: SnackbarViewModel
) {
    val sheetState = rememberModalBottomSheetState()
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight().verticalScroll(scrollState),
        sheetState = sheetState,
        onDismissRequest = { onChange(false) }
    ) {
        AddTODOForm(
            modifier = Modifier.padding(16.dp),
            todoViewModel = todoViewModel,
            snackbarViewModel = snackbarViewModel,
            todoItem = todoItem,
            closeSheet = { onChange(false) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTODOForm(
    modifier: Modifier = Modifier,
    todoViewModel: TODOViewModel,
    snackbarViewModel: SnackbarViewModel,
    todoItem: TODOItem? = null,
    closeSheet: () -> Unit
 ) {
    val context = LocalContext.current

    var dropDownExpanded by remember { mutableStateOf(false)}
    val dropDownItems = listOf(
        PriorityLevel.LOW to "Low Priority",
        PriorityLevel.NORMAL to "Normal Priority",
        PriorityLevel.HIGH to "High Priority"
        )

    var datePickerExpanded by remember { mutableStateOf(false) }
    var dueDatePickerExpanded by remember { mutableStateOf(false) }

    var isTitleError by rememberSaveable { mutableStateOf(false) }
    var isDatesError by rememberSaveable { mutableStateOf(false) }

    val initialTitle = todoItem?.title ?: ""
    val initialDescription = todoItem?.description ?: ""
    val initialCategory = todoItem?.category ?: ""
    val initialReminder = todoItem?.reminder ?: false
    val initialPriorityLevel = todoItem?.priorityLevel ?: PriorityLevel.LOW
    val initialSelectedIndex = todoItem?.priorityLevel?.ordinal ?: 0
    val initialCreationDate = todoItem?.createdDate ?: LocalDate.now()
    val initialDueDate = todoItem?.dueDate

    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }
    var category by remember { mutableStateOf(initialCategory) }
    var reminder by remember { mutableStateOf(initialReminder) }
    var priorityLevel by remember { mutableStateOf(initialPriorityLevel) }
    var selectedIndex by remember { mutableIntStateOf(initialSelectedIndex) }
    var creationDate by remember { mutableStateOf(initialCreationDate) }
    var dueDate by remember { mutableStateOf(initialDueDate) }

    fun validateTitle(text: String) {
        isTitleError = text == ""
    }
    fun validateDates(taskDate: LocalDate, dueDate: LocalDate?) {
        if(dueDate == null) {
            isDatesError = false
            return
        }
        if (taskDate > dueDate) {
            isDatesError = true
            return
        } else {
            isDatesError = false
        }
    }

    Column(modifier = modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Title")},
            value = title,
            onValueChange = {
                title = it
                validateTitle(title)
                            },
            isError = isTitleError,
            supportingText = {
                if(isTitleError){
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Title can't be empty.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (isTitleError) {
                    Icon(Icons.Rounded.Error, "Error", tint = MaterialTheme.colorScheme.error)
                }
            },
            keyboardActions = KeyboardActions { validateTitle(title) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Description")},
            value = description,
            onValueChange = {description = it},
            )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Category")},
            value = category,
            onValueChange = {category = it},
            keyboardOptions = KeyboardOptions(
                imeAction =  ImeAction.Done
            ),
            singleLine = true

        )
        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = dropDownExpanded,
            onExpandedChange = {dropDownExpanded = !dropDownExpanded},
            modifier = Modifier
        ) {
            TextField(
                readOnly = true,
                label = {Text("Priority")},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = dropDownItems[selectedIndex].second,
                onValueChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.
                    TrailingIcon(expanded = dropDownExpanded)}
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

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TextField(
                readOnly = true,
                label = { Text("Task Date") },
                modifier = Modifier.weight(1f),
                value = creationDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                onValueChange = {
                    validateDates(taskDate = creationDate, dueDate = dueDate)
                },
                trailingIcon = {
                    IconButton(onClick = { datePickerExpanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Select task date"
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledTextColor = LocalContentColor.current.copy(alpha = 1f),
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                enabled = false
            )

            TextField(
                readOnly = true,
                label = { Text("Due Date") },
                modifier = Modifier.weight(1f),
                value = dueDate?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) ?: "",
                onValueChange = {
                    validateDates(taskDate = creationDate, dueDate = dueDate)
                },
                trailingIcon = {
                    IconButton(onClick = { dueDatePickerExpanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Select due date"
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledTextColor = LocalContentColor.current.copy(alpha = 1f),
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                enabled = false,
                isError = isDatesError,
                supportingText = {
                    if (isDatesError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Due date can't be earlier than the task date!",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            IconButton(onClick = {reminder = !reminder}) {
                Icon(
                    imageVector =
                    if (reminder)
                        Icons.Rounded.NotificationsActive
                    else
                        Icons.Outlined.NotificationsOff,
                    contentDescription = "Notifications")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    validateTitle(title)
                    validateDates(taskDate = creationDate, dueDate = dueDate)
                    if(!isTitleError && !isDatesError)
                    {
                        todoViewModel.upsertTODOItem(
                            TODOItem(
                                id = todoItem?.id ?: 0,
                                title = title,
                                description = description,
                                createdDate = creationDate,
                                dueDate = dueDate,
                                category = category,
                                reminder = reminder,
                                priorityLevel = priorityLevel
                            )
                        )
                        closeSheet()
                        val todoSheetSnackbarText = if(todoItem != null) "TODO is successfully updated" else "TODO is successfully created!"
                        snackbarViewModel.showSnackbar(todoSheetSnackbarText)
                    } else {
                        if(title == "") {
                            Toast.makeText(context, "TODO cannot be created without a title!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Due Date cannot be earlier than the Task Date!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }) {
                val todoSheetButtonText = if (todoItem != null) "Update TODO" else "Add TODO"
                Text(todoSheetButtonText)
            }
        }

        if (dueDatePickerExpanded) {
            TODODatePicker(
                onDismiss = { dueDatePickerExpanded = false },
                onDateSelected = {
                    dueDate = it
                    dueDatePickerExpanded = false
                },
                initialDateMillis = creationDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        }
        if (datePickerExpanded) {
            val now = LocalDate.now()
            val startOfDayNow = now.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            TODODatePicker(
                onDismiss = { datePickerExpanded = false },
                onDateSelected = {
                    creationDate = it
                    datePickerExpanded = false
                },
                initialDateMillis =

                if (dueDate == null || (dueDate ?: now) >= now) {
                    startOfDayNow
                } else {
                    dueDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli() ?: startOfDayNow
                }

            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TODODatePicker(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
    initialDateMillis: Long = 0
) {

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null,
        initialDisplayedMonthMillis = initialDateMillis,
        selectableDates = FutureSelectableDates(initialDateMillis)
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = datePickerState.selectedDateMillis
                    val date =
                        if (millis != null) {
                            Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        } else {
                            LocalDate.now()
                        }
                    onDateSelected(date)
                    onDismiss()
                },
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class FutureSelectableDates(
    private val dayStart: Long
): SelectableDates {
    private val now = LocalDate.now()
    //private val dayStart2 = now.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= dayStart
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= now.year
    }
}