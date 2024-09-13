package com.egelsia.todomore.viewmodels

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SnackbarViewModel : ViewModel(){
    private val _snackbarMessage = mutableStateOf<String?>(null)
    val snackbarMessage = _snackbarMessage

    private val _snackbarDuration = mutableStateOf(SnackbarDuration.Short)
    val snackbarDuration = _snackbarDuration

    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        _snackbarMessage.value = message
        _snackbarDuration.value = duration
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}

