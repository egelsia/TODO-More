package com.egelsia.todomore.data.user

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    var userName: String
        get() = sharedPreferences.getString(KEY_USER_NAME,null) ?: ""
        set(value) = sharedPreferences.edit().putString(KEY_USER_NAME, value).apply()

    var completedTodos: Int
        get() = sharedPreferences.getInt(KEY_COMPLETED_TODOS, 0)
        set(value) = sharedPreferences.edit().putInt(KEY_COMPLETED_TODOS, value).apply()

    var ongoingTodos: Int
        get() = sharedPreferences.getInt(KEY_ONGOING_TODOS, 0)
        set(value) = sharedPreferences.edit().putInt(KEY_ONGOING_TODOS, value).apply()

    var deletedTodos: Int
        get() = sharedPreferences.getInt(KEY_DELETED_TODOS, 0)
        set(value) = sharedPreferences.edit().putInt(KEY_DELETED_TODOS, value).apply()

    var totalTodos: Int
        get() = sharedPreferences.getInt(KEY_TOTAL_TODOS, 0)
        set(value) = sharedPreferences.edit().putInt(KEY_TOTAL_TODOS, value).apply()

    var totalTime: Long
        get() = sharedPreferences.getLong(KEY_TOTAL_TIME, 0)
        set(value) = sharedPreferences.edit().putLong(KEY_TOTAL_TIME, value).apply()

    var isDialogShown: Boolean
        get() = sharedPreferences.getBoolean(KEY_DIALOG_SHOWN, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_DIALOG_SHOWN, value).apply()

    fun userExists(): Boolean = userName.isNotEmpty()

    fun createUser(name: String) {
        userName = name
        isDialogShown = true
    }

    fun incrementCompletedTodos() = sharedPreferences.edit().putInt(KEY_COMPLETED_TODOS, completedTodos + 1).apply()
    fun incrementOngoingTodos() = sharedPreferences.edit().putInt(KEY_ONGOING_TODOS, ongoingTodos + 1).apply()
    fun incrementDeletedTodos() = sharedPreferences.edit().putInt(KEY_DELETED_TODOS, deletedTodos + 1).apply()
    fun incrementTotalTodos() = sharedPreferences.edit().putInt(KEY_TOTAL_TODOS, totalTodos + 1).apply()
    fun incrementTotalTime(amount: Long) = sharedPreferences.edit().putLong(KEY_TOTAL_TIME, totalTime + amount).apply()

    companion object {
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_COMPLETED_TODOS = "completed_todos"
        private const val KEY_ONGOING_TODOS = "ongoing_todos"
        private const val KEY_DELETED_TODOS = "deleted_todos"
        private const val KEY_TOTAL_TODOS = "total_todos"
        private const val KEY_TOTAL_TIME = "total_time"
        private const val KEY_DIALOG_SHOWN = "dialog_shown"
    }
}