package com.example.userdirectory.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // UI reads from Room (Flow)
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun observeAll(): Flow<List<UserEntity>>

    //Local search by name or email
    @Query("""
        SELECT * FROM users
        WHERE name LIKE :q OR email LIKE :q
        ORDER BY id ASC
    """)
    fun observeSearch(q: String): Flow<List<UserEntity>>

    //Upsert with REPLACE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(users: List<UserEntity>)
}
