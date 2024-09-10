package com.egelsia.todomore.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.egelsia.todomore.data.todo.TODOItem
import com.egelsia.todomore.data.todo.TODOItemDao

@Database(
    entities = [TODOItem::class, User::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TodomoreDatabase: RoomDatabase() {

    abstract val todoItemDao: TODOItemDao
    abstract val userDao: UserDao

    companion object {
        @Volatile
        private var INSTANCE: TodomoreDatabase? = null

        fun getDatabase(context: Context): TodomoreDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodomoreDatabase::class.java,
                    "todomore_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}