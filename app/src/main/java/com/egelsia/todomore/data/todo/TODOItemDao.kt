package com.egelsia.todomore.data.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TODOItemDao {
    @Upsert
    suspend fun upsertTODOItem(todoItem: TODOItem)

    @Delete
    suspend fun deleteTODOItem(todoItem: TODOItem)

    @Query("DELETE FROM todo_table WHERE id =:id")
    suspend fun deleteTODOItemById(id: Int)

    @Query("SELECT * FROM todo_table ORDER BY dueDate ASC")
    fun getItemsOrderedByDueDate() : Flow<List<TODOItem>>

    @Query("SELECT * FROM todo_table ORDER BY priorityLevel ASC")
    fun getItemsOrderedByPriorityLevel() : Flow<List<TODOItem>>

    @Query("SELECT * FROM todo_table ORDER BY createdDate ASC")
    fun getItemsOrderedByCreatedDate() : Flow<List<TODOItem>>

    @Query("SELECT * FROM todo_table ORDER BY status DESC")
    fun getItemsOrderedByStatus() : Flow<List<TODOItem>>

    @Query("SELECT * FROM todo_table ORDER BY category ASC")
    fun getItemsOrderedByCategory(): Flow<List<TODOItem>>

    @Query("SELECT * FROM todo_table WHERE id = :id")
    fun getItemById(id: Int) : Flow<TODOItem>

    @Query("SELECT * FROM todo_table WHERE createdDate =:date OR dueDate =:date")
    fun getListByDate(date: LocalDate): Flow<List<TODOItem>>

    @Query("SELECT * FROM todo_table WHERE createdDate BETWEEN :firstDate AND :lastDate OR dueDate BETWEEN :firstDate AND :lastDate")
    fun getListBetweenDates(firstDate: LocalDate, lastDate: LocalDate): Flow<List<TODOItem>>
}