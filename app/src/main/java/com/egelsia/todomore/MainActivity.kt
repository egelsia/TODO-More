package com.egelsia.todomore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.egelsia.todomore.data.TodomoreDatabase
import com.egelsia.todomore.data.todo.TODOItemDao
import com.egelsia.todomore.screens.TodoApp
import com.egelsia.todomore.ui.theme.ToDoMoreTheme

class MainActivity : ComponentActivity() {

    private lateinit var todoItemDao: TODOItemDao
    private lateinit var db: TodomoreDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TodomoreDatabase.getDatabase(this)
        todoItemDao = db.todoItemDao
        enableEdgeToEdge()
        setContent {
            ToDoMoreTheme {
                TodoApp(todoItemDao = todoItemDao)
            }
        }
    }
}