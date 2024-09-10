package com.egelsia.todomore.data.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "todo_table")
data class TODOItem(
    val title: String = "",
    val description: String = "",
    val createdDate: Date = Date(System.currentTimeMillis()),
    val dueDate: Date? = null,
    val completionDate: Date? = null,
    val category: String = "",
    val priorityLevel: PriorityLevel = PriorityLevel.LOW,
    val status: TODOStatus = TODOStatus.TODO,
    val reminder: Boolean = false,
    //val color: Color = Color.Transparent,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)