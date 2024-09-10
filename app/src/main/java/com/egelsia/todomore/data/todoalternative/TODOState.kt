//package com.egelsia.todomore.data.todoalternative
//
//import androidx.compose.ui.graphics.Color
//import com.egelsia.todomore.data.todo.PriorityLevel
//import com.egelsia.todomore.data.todo.SortType
//import com.egelsia.todomore.data.todo.TODOItem
//import com.egelsia.todomore.data.todo.TODOStatus
//import java.util.Date
//
//data class TODOState(
//    val todos: List<TODOItem> = emptyList(),
//    val title: String = "",
//    val description: String = "",
//    val createdDate: Date = Date(System.currentTimeMillis()),
//    val dueDate: Date? = null,
//    val completionDate: Date? = null,
//    val category: String = "",
//    val priorityLevel: PriorityLevel = PriorityLevel.LOW,
//    val todoStatus: TODOStatus = TODOStatus.TODO,
//    val color: Color = Color.Transparent,
//    val reminder: Boolean = false,
//    val isAddingTODO: Boolean = false,
//    val sortType: SortType = SortType.CREATION_DATE
//)
