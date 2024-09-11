package com.egelsia.todomore.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userDao: UserDao) : ViewModel() {
    private val _userState = MutableStateFlow<StateHolder<User>>(StateHolder.Loading)
    val userState: StateFlow<StateHolder<User>> = _userState

    fun fetchUser() {
        viewModelScope.launch {
            _userState.value = StateHolder.Loading
            try {
                val user = userDao.getUser().firstOrNull()
                if (user != null) {
                    _userState.value = StateHolder.Success(user)
                } else {
                    _userState.value = StateHolder.Error("No user found.")
                }
            } catch (e: Exception) {
                _userState.value = StateHolder.Error(e.message ?: "Unknown error occurred.")
            }
        }
    }

    fun upsertUser(user: User) {
        viewModelScope.launch {
            try {
                userDao.upsertUser(user)
                _userState.value = StateHolder.Success(user)
            } catch (e: Exception) {
                _userState.value = StateHolder.Error(e.message ?: "Unknown error occurred.")
            }
        }
    }
}