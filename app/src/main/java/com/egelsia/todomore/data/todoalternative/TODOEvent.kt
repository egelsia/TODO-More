//package com.egelsia.todomore.data.todoalternative
//
//import androidx.compose.ui.graphics.Color
//import com.egelsia.todomore.data.todo.PriorityLevel
//import com.egelsia.todomore.data.todo.SortType
//import com.egelsia.todomore.data.todo.TODOItem
//import com.egelsia.todomore.data.todo.TODOStatus
//import java.util.Date
//
//sealed interface TODOEvent {
//    object SaveTODO: TODOEvent
//    data class SetTitle(val title: String): TODOEvent
//    data class SetDescription(val description: String): TODOEvent
//    data class SetDueDate(val dueDate: Date): TODOEvent
//    data class SetCompletionDate(val completionDate: Date): TODOEvent
//    data class SetCategory(val category: String): TODOEvent
//    data class SetPriority(val priorityLevel: PriorityLevel): TODOEvent
//    data class SetStatus(val todoStatus: TODOStatus): TODOEvent
//    data class SetReminder(val reminder: Boolean): TODOEvent
//    data class SetColor(val color: Color): TODOEvent
//    object ShowSheet: TODOEvent
//    object HideSheet: TODOEvent
//    data class SortTODOs(val sortType: SortType) : TODOEvent
//    data class DeleteTODO(val todoItem: TODOItem): TODOEvent
//}