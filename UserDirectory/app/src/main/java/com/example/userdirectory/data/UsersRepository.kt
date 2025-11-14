package com.example.userdirectory.data

import com.example.userdirectory.data.local.UserDao
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.remote.JsonPlaceholderApi
import kotlinx.coroutines.flow.Flow

class UsersRepository(
    private val api: JsonPlaceholderApi,
    private val dao: UserDao
) {
    // 3.1 UI reads from Room flows (not from network)
    fun observeAll(): Flow<List<UserEntity>> = dao.observeAll()

    // 5.3 Local search via DAO
    fun search(query: String): Flow<List<UserEntity>> =
        dao.observeSearch("%$query%")

    // 4.x Refresh from network → update Room
    suspend fun refresh() {
        val remote = api.getUsers()
        dao.upsertAll(remote.map { it.toEntity() })
    }
}
