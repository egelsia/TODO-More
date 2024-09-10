package com.egelsia.todomore.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    val userName: String = "User",
    val completedTodos: Int = 0,
    val ongoingTodos: Int = 0,
    val deletedTodos: Int = 0,
    val totalTodos: Int = 0,
    val totalTime: Long = 0L,
    @PrimaryKey(autoGenerate = true) val id : Int = 0
)
