package com.egelsia.todomore.data

sealed class StateHolder<out T> {
    object Loading : StateHolder<Nothing>()
    data class Success<T>(val data: T) : StateHolder<T>()
    data class Error(val message: String) : StateHolder<Nothing>()
}