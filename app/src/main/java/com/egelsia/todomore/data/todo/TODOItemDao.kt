package com.egelsia.todomore.data.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TODOItemDao {
    @Upsert
    suspend fun upsertTODOItem(todoItem: TODOItem)

    @Delete
    suspend fun deleteTODOItem(todoItem: TODOItem)

    @Query("SELECT * FROM todo_table ORDER BY dueDate ASC")
    fun getItemsOrderedByDueDate() : Flow<List<TODOItem>>

    //@Query("SELECT * FROM todo_table ORDER BY priorityLevel ASC")
    //fun getItemsOrderedByPriorityLevel() : Flow<List<TODOItem>>

    @Query("SELECT * FROM todo_table ORDER BY createdDate ASC")
    fun getItemsOrderedByCreatedDate() : Flow<List<TODOItem>>

    //@Query("SELECT * FROM todo_table ORDER BY status DESC")
    //fun getItemsOrderedByStatus() : Flow<List<TODOItem>>

    @Query("SELECT * FROM todo_table ORDER BY category ASC")
    fun getItemsOrderedByCategory(): Flow<List<TODOItem>>
}