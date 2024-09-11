package com.egelsia.todomore.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUser(user: User)

    @Query("SELECT * FROM user")
    suspend fun getUser() : List<User>

    @Query("SELECT COUNT(*) FROM user")
    suspend fun getUserCount(): Int
}