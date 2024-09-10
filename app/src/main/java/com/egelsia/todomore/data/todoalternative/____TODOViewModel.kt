//package com.egelsia.todomore.data.todoalternative
//
//import androidx.compose.ui.graphics.Color
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.egelsia.todomore.data.todo.PriorityLevel
//import com.egelsia.todomore.data.todo.SortType
//import com.egelsia.todomore.data.todo.TODOItem
//import com.egelsia.todomore.data.todo.TODOItemDao
//import com.egelsia.todomore.data.todo.TODOStatus
//import com.egelsia.todomore.data.UserDao
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.flatMapLatest
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class ____TODOViewModel(private val todoItemDao: TODOItemDao, private val userDao: UserDao) : ViewModel() {
//    private val _sortType = MutableStateFlow(SortType.CREATION_DATE)
//    private val _todos = _sortType
//        .flatMapLatest { sortType ->
//            when(sortType) {
//                SortType.CREATION_DATE -> todoItemDao.getItemsOrderedByCreatedDate()
//                SortType.DUE_DATE -> todoItemDao.getItemsOrderedByDueDate()
//                SortType.CATEGORY -> todoItemDao.getItemsOrderedByCategory()
//                SortType.PRIORITY_LEVEL -> todoItemDao.getItemsOrderedByPriorityLevel()
//                SortType.STATUS -> todoItemDao.getItemsOrderedByStatus()
//            }
//        }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList<TODOItem>())
//
//    private val _state = MutableStateFlow(TODOState())
//    val state = combine(_state, _sortType, _todos) {state, sortType, todos ->
//        state.copy(
//            todos = todos,
//            sortType = sortType
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), TODOState())
//
//
//    fun onEvent(event: TODOEvent) {
//        when (event) {
//            is TODOEvent.DeleteTODO -> {
//                viewModelScope.launch {
//                    todoItemDao.deleteTODOItem(event.todoItem)
//                    val user = userDao.getUser()
//                    if(user.isNotEmpty()) {
//                        userDao.upsertUser(user[0].copy(deletedTodos = user[0].deletedTodos + 1))
//                    }
//
//                }
//            }
//            TODOEvent.HideSheet -> {
//                _state.update { it.copy(isAddingTODO = false) }
//            }
//            TODOEvent.SaveTODO -> {
//                val title = state.value.title
//                val description = state.value.description
//                val createdDate = state.value.createdDate
//                val dueDate = state.value.dueDate
//                val completionDate = state.value.completionDate
//                val category = state.value.category
//                val priorityLevel = state.value.priorityLevel
//                val status = state.value.todoStatus
//                val reminder = state.value.reminder
//                val color = state.value.color
//
//                if (title.isBlank() || description.isBlank() || category.isBlank()) {
//                    return
//                }
//
//                val todoItem = TODOItem(
//                    title = title,
//                    description = description,
//                    createdDate = createdDate,
//                    dueDate = dueDate,
//                    completionDate = completionDate,
//                    category = category,
//                    priorityLevel = priorityLevel,
//                    status = status,
//                    reminder = reminder,
//                    color = color
//                )
//                viewModelScope.launch {
//                    todoItemDao.upsertTODOItem(todoItem)
//                }
//                _state.update {
//                    it.copy(
//                        title = "",
//                        description = "",
//                        dueDate = null,
//                        completionDate = null,
//                        category = "",
//                        priorityLevel = PriorityLevel.LOW,
//                        todoStatus = TODOStatus.TODO,
//                        color = Color.Transparent,
//                        reminder = false,
//                        isAddingTODO = false
//                    )
//                }
//
//            }
//            is TODOEvent.SetCategory -> {
//                _state.update { it.copy(category = event.category) }
//            }
//            is TODOEvent.SetColor -> {
//                _state.update { it.copy(color = event.color)}
//            }
//            is TODOEvent.SetCompletionDate -> {
//                _state.update {it.copy(completionDate = event.completionDate)}
//            }
//            is TODOEvent.SetDescription -> {
//                _state.update {it.copy(description = event.description)}
//            }
//            is TODOEvent.SetDueDate -> {
//                _state.update {it.copy(dueDate = event.dueDate)}
//            }
//            is TODOEvent.SetPriority -> {
//                _state.update {it.copy( priorityLevel = event.priorityLevel)}
//            }
//            is TODOEvent.SetReminder -> {
//                _state.update {it.copy(reminder = event.reminder)}
//            }
//            is TODOEvent.SetStatus -> {
//                _state.update {it.copy(todoStatus = event.todoStatus)}
//            }
//            is TODOEvent.SetTitle -> {
//                _state.update { it.copy(title = event.title) }
//            }
//            TODOEvent.ShowSheet -> {
//                _state.update { it.copy(isAddingTODO = true) }
//            }
//            is TODOEvent.SortTODOs -> {
//                _sortType.value = event.sortType
//            }
//        }
//    }
//}