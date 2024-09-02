package com.egelsia.todomore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.egelsia.todomore.screens.TodoApp
import com.egelsia.todomore.ui.theme.ToDoMoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoMoreTheme {
                TodoApp()
            }
        }
    }
}