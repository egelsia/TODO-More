package com.egelsia.todomore.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egelsia.todomore.data.todo.TODOItem
import com.egelsia.todomore.data.todo.TODOItemDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TODOViewModel(private val todoItemDao: TODOItemDao) : ViewModel() {
    private val _todoState = MutableStateFlow<StateHolder<Flow<List<TODOItem>>>>(StateHolder.Loading)
    val todoState: StateFlow<StateHolder<Flow<List<TODOItem>>>> = _todoState

    fun getListOrderedByCreatedDate() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val todoList = todoItemDao.getItemsOrderedByCreatedDate()
                _todoState.value = StateHolder.Success<Flow<List<TODOItem>>>(todoList)
            } catch (e: Exception) {
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of logs.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

//    fun getListOrderedByPriorityLevel() {
//        viewModelScope.launch {
//            _todoState.value = StateHolder.Loading
//            try {
//                val todoList = todoItemDao.getItemsOrderedByPriorityLevel()
//                _todoState.value = StateHolder.Success<Flow<List<TODOItem>>>(todoList)
//            } catch (e: Exception) {
//                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of logs.")
//                StateHolder.Error(e.message ?: "An unknown error occurred.")
//            }
//        }
//    }

    fun getListOrderedByCreatedCategory() {
        viewModelScope.launch {
            _todoState.value = StateHolder.Loading
            try {
                val todoList = todoItemDao.getItemsOrderedByCategory()
                _todoState.value = StateHolder.Success<Flow<List<TODOItem>>>(todoList)
            } catch (e: Exception) {
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of logs.")
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
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of logs.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

//    fun getListOrderedByStatus() {
//        viewModelScope.launch {
//            _todoState.value = StateHolder.Loading
//            try {
//                val todoList = todoItemDao.getItemsOrderedByStatus()
//                _todoState.value = StateHolder.Success<Flow<List<TODOItem>>>(todoList)
//            } catch (e: Exception) {
//                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of logs.")
//                StateHolder.Error(e.message ?: "An unknown error occurred.")
//            }
//        }
//    }

    fun upsertTODOItem(todoItem: TODOItem) {
        viewModelScope.launch {
            try {
                Log.d("TODOVIEWMODEL", "Try block")
                todoItemDao.upsertTODOItem(todoItem)
            } catch (e: Exception) {
                Log.e("TODOVIEWMODEL", e.message ?: "An exception occurred while getting the value of logs.")
                StateHolder.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }
}