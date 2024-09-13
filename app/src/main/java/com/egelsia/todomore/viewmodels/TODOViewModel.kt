package com.egelsia.todomore.viewmodels

import android.content.Context
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

class TODOViewModel(private val todoItemDao: TODOItemDao, private val userPreferences: UserPreferences) : ViewModel() {
    private val _todoState = MutableStateFlow<StateHolder<Flow<List<TODOItem>>>>(StateHolder.Loading)
    val todoState: StateFlow<StateHolder<Flow<List<TODOItem>>>> = _todoState

    fun getListOrderedByCreatedDate() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val todoList = todoItemDao.getItemsOrderedByCreatedDate()
                _todoState.value = StateHolder.Success<Flow<List<TODOItem>>>(todoList)
            } catch (e: Exception) {
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of todos.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
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
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of todos.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
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
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of todos.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
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
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of todos.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
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
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of todos.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun upsertTODOItem(todoItem: TODOItem) {
        viewModelScope.launch {
            try {
                todoItemDao.upsertTODOItem(todoItem)
            } catch (e: Exception) {
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while adding a new todo.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun deleteTODOItem(todoItem: TODOItem) {
        viewModelScope.launch {
            try {
                todoItemDao.deleteTODOItem(todoItem)
                userPreferences.incrementDeletedTodos()
            }
            catch (e: Exception) {
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while adding a new todo.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun deleteTODOItemById(id: Int){
        viewModelScope.launch {
            try {
                todoItemDao.deleteTODOItemById(id)
                userPreferences.incrementDeletedTodos()
            } catch (e: Exception) {
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while deleting a todo.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }
}