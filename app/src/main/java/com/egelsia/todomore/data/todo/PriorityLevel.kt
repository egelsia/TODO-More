package com.egelsia.todomore.data.todo

import kotlinx.serialization.Serializable

@Serializable
enum class PriorityLevel {
    LOW, NORMAL, HIGH
}