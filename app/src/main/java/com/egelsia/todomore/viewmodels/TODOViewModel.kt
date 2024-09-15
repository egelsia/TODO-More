package com.egelsia.todomore.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egelsia.todomore.data.StateHolder
import com.egelsia.todomore.data.todo.TODOItem
import com.egelsia.todomore.data.todo.TODOItemDao
import com.egelsia.todomore.data.user.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class TODOViewModel(
    private val todoItemDao: TODOItemDao,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _todoState =
        MutableStateFlow<StateHolder<Flow<List<TODOItem>>>>(StateHolder.Loading)
    val todoState: StateFlow<StateHolder<Flow<List<TODOItem>>>> = _todoState

    private val _singleTodoState = MutableStateFlow<StateHolder<Flow<TODOItem>>>(StateHolder.Loading)
    val singleTodoState: StateFlow<StateHolder<Flow<TODOItem>>> = _singleTodoState

    fun getListOrderedByCreatedDate() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val todoList = todoItemDao.getItemsOrderedByCreatedDate()
                _todoState.value = StateHolder.Success<Flow<List<TODOItem>>>(todoList)
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while getting the value of todos."
                )
                _todoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun getListOrderedByPriorityLevel() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val todoList = todoItemDao.getItemsOrderedByPriorityLevel()
                _todoState.value = StateHolder.Success<Flow<List<TODOItem>>>(todoList)
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while getting the value of todos."
                )
                _todoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun getListOrderedByCreatedCategory() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val todoList = todoItemDao.getItemsOrderedByCategory()
                _todoState.value = StateHolder.Success<Flow<List<TODOItem>>>(todoList)
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while getting the value of todos."
                )
                _todoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun getListOrderedByDueDate() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val todoList = todoItemDao.getItemsOrderedByDueDate()
                _todoState.value = StateHolder.Success<Flow<List<TODOItem>>>(todoList)
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while getting the value of todos."
                )
                _todoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun getListOrderedByStatus() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val todoList = todoItemDao.getItemsOrderedByStatus()
                _todoState.value = StateHolder.Success<Flow<List<TODOItem>>>(todoList)
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while getting the value of todos."
                )
                _todoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun upsertTODOItem(todoItem: TODOItem) {
        viewModelScope.launch {
            try {
                todoItemDao.upsertTODOItem(todoItem)
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while adding a new todo."
                )
                _todoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun deleteTODOItem(todoItem: TODOItem) {
        viewModelScope.launch {
            try {
                todoItemDao.deleteTODOItem(todoItem)
                userPreferences.incrementDeletedTodos()
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while adding a new todo."
                )
                _todoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun getTODOItemById(id: Int) {
        viewModelScope.launch {
            _singleTodoState.value = StateHolder.Loading
            try {
                val todoItem = todoItemDao.getItemById(id)
                _singleTodoState.value = StateHolder.Success(todoItem)
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while getting the todo item."
                )
                _singleTodoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun getListOfToday() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val now = LocalDate.now()
                val firstDay = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val lastDay = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                val todoList = todoItemDao.getListBetweenDates(firstDay, lastDay)
                _todoState.value = StateHolder.Success(todoList)
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while getting the todo item."
                )
                _todoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun getListOfThisMonth() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val now = LocalDate.now()
                val firstDay = now.with(TemporalAdjusters.firstDayOfMonth())
                val lastDay = now.with(TemporalAdjusters.lastDayOfMonth())
                val todoList = todoItemDao.getListBetweenDates(firstDay, lastDay)
                _todoState.value = StateHolder.Success(todoList)
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while getting the todo item."
                )
                _todoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun getListOfThisWeek() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val todoList = todoItemDao.getListByDate(LocalDate.now())
                _todoState.value = StateHolder.Success(todoList)
            } catch (e: Exception) {
                Log.e(
                    "TODOVIEWMODEL",
                    e.message ?: "An exception occurred while getting the todo item."
                )
                _todoState.value = StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }
}