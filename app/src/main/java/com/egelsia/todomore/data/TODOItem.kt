package com.egelsia.todomore.data

import androidx.compose.ui.graphics.Color
import java.util.Date

data class TODOItem(
    val title: String,
    val description: String,

    val createdDate: Date,
    val dueDate: Date,
    val completionDate: Date,

    val category: String,
    val priorityLevel: PriorityLevel,
    val status: TODOStatus,
    val reminder: Boolean,
    val color: Color
)
{
}