package com.egelsia.todomore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Pending
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egelsia.todomore.data.user.UserPreferences

@Preview
@Composable
fun UserView(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    var showUserNameDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Rounded.AccountCircle,
                        "Account"
                    )
                },
                overlineContent = {
                    Text(text = "Username")
                },
                headlineContent = {
                    Text(text = userPreferences.userName)
                },
                trailingContent = {
                    IconButton(
                        onClick = { showUserNameDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit Username"
                        )
                    }
                }
            )
            HorizontalDivider()
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Rounded.Timer,
                        "Account"
                    )
                },
                overlineContent = {
                    Text("Total Time")
                },
                headlineContent = {
                    Text(
                        text = userPreferences.totalTime.toString(),
                    )
                }
            )
            HorizontalDivider()
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Rounded.Done,
                        "Completed To Do's"
                    )
                },
                overlineContent = { Text(text = "Completed TODOs")},
                headlineContent = {
                    Text(text = userPreferences.completedTodos.toString(),)
                }
            )

            HorizontalDivider()
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Rounded.Pending,
                        "Ongoing To Do's"
                    )
                },
                overlineContent = {
                    Text("Ongoing TODOs")
                },
                headlineContent = {
                    Text(text = userPreferences.ongoingTodos.toString())
                }
            )
            HorizontalDivider()
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Rounded.Delete,
                        "Deleted To Do's"
                    )
                },
                overlineContent = {
                    Text("Deleted TODOs")
                },
                headlineContent = {
                    Text(text = userPreferences.deletedTodos.toString())
                }
            )
            HorizontalDivider()
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        "Total To Do count"
                    )
                },
                overlineContent = {
                    Text("Total TODOs")
                },
                headlineContent = {
                    Text(text = userPreferences.totalTodos.toString())
                }

            )
        }
    }

    if (showUserNameDialog) {
        UsernameDialog(
            onDismissRequest = { showUserNameDialog = false },
            onConfirmation = {
                userPreferences.userName = it
                showUserNameDialog = false
            }
        )
    }
}