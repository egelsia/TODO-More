package com.egelsia.todomore.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


data class CustomIcon(
    val outlined: ImageVector,
    val filled: ImageVector,
    val route: String,
    val description: String,
)


@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
) {

    val navList = listOf(
        CustomIcon(Icons.Outlined.Today, Icons.Rounded.Today, "main", "Today"),
        CustomIcon(Icons.Outlined.DateRange, Icons.Rounded.DateRange, "main", "Weekly"),
        CustomIcon(Icons.Outlined.Timer, Icons.Rounded.Timer, "main", "Timer"),
        CustomIcon(Icons.Outlined.CalendarToday, Icons.Rounded.CalendarToday, "main", "Monthly"),
        CustomIcon(Icons.Rounded.Checklist, Icons.Rounded.Checklist, "main", "All")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        navList.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedItem == index) item.filled else item.outlined,
                        contentDescription = item.description,
                    )
                },
                label = { Text(item.description, maxLines = 1 , textAlign = TextAlign.Center) },
                selected = selectedItem == index,
                onClick = {
                    onItemSelected(index)
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}