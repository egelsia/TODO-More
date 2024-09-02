package com.egelsia.todomore.data

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import java.io.InputStream
import java.io.OutputStream
import java.util.Date

data class TODOItem(
    val id: Int = -1,
    val title: String = "Default",
    val description: String = "Default description",

    val createdDate: Date = Date(System.currentTimeMillis()),
    val dueDate: Date? = null,
    val completionDate: Date? = null,

    val category: String = "",
    val priorityLevel: PriorityLevel = PriorityLevel.LOW,
    val status: TODOStatus = TODOStatus.TODO,
    val reminder: Boolean = false,
    val color: Color = Color.Red
)

data class TODOItemList (
    val items: List<TODOItem> = emptyList()
)

object TODOItemListSerializer : Serializer<TODOItemList> {
    override val defaultValue: TODOItemList
        get() = TODOItemList()

    override suspend fun readFrom(input: InputStream): TODOItemList {
        return try {
            val bytes = input.readBytes()
            Json.decodeFromString<TODOItemList>(bytes.toString(Charsets.UTF_8))
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: TODOItemList, output: OutputStream) {
        val jsonString = Json.encodeToString(t)
        withContext(Dispatchers.IO) {
            output.write(jsonString.toByteArray(Charsets.UTF_8))
        }
    }
}

val Context.todoDataStore: DataStore<TODOItemList> by dataStore(
    fileName = "todo_items.pb",
    serializer = TODOItemListSerializer
)

enum class UpdateType {
    REMOVE, UPDATE, ADD
}

suspend fun UpdateTODO(context: Context, todoItem: TODOItem, updateType: UpdateType) {
    context.todoDataStore.updateData { currentTODOList ->
        val updatedList = currentTODOList.items.toMutableList().apply {
            if (updateType == UpdateType.ADD) {
                add(todoItem)
            } else if (updateType == UpdateType.REMOVE) {
                val todoItemToBeRemoved = find { it.id == todoItem.id }
                remove(todoItemToBeRemoved)
            } else if (updateType == UpdateType.UPDATE) {
                val updatedTODOItem = find {it.id == todoItem.id}
                remove(updatedTODOItem)
                add(todoItem)
            }
        }
        TODOItemList(updatedList)
    }
}

suspend fun getTODOItemList(context: Context) : List<TODOItem> {
    return context.todoDataStore.data.first().items
}

suspend fun isTODOItemInStorage(context: Context, todoItem: TODOItem) : Boolean {
    val foundedItem = context.todoDataStore.data.first().items.find { it.id == todoItem.id }
    return foundedItem != null
}

suspend fun getTODOItemById(context: Context, id: Int) : TODOItem? {
    return context.todoDataStore.data.first().items.find{
        it.id == id
    }
}