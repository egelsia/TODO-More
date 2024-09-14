package com.egelsia.todomore.data.todo

import kotlinx.serialization.Serializable

@Serializable
enum class TODOStatus {
    TODO, PROGRESS, COMPLETED
}