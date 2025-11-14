package com.example.userdirectory.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.userdirectory.data.UsersRepository
import com.example.userdirectory.data.local.AppDb
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.remote.Network
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val users: List<UserEntity> = emptyList(),
    val query: String = ""
)

class UserViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UsersRepository(
        api = Network.api,
        dao = AppDb.create(app).userDao()
    )

    private val query = MutableStateFlow("")
    private val loading = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)

    private val usersFlow: Flow<List<UserEntity>> =
        query.flatMapLatest { q ->
            if (q.isBlank()) repo.observeAll() else repo.search(q)
        }

    // Expose combined UI state
    val state: StateFlow<UiState> = combine(usersFlow, loading, error, query) { users, l, e, q ->
        UiState(isLoading = l, error = e, users = users, query = q)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState(isLoading = true))

    //Refeshing the View Screen when upload new data into JSONPlaceholder
    init {
        refresh()
    }

    fun setQuery(q: String) { query.value = q }

    fun refresh() = viewModelScope.launch {
        loading.value = true
        error.value = null
        try {
            repo.refresh()      //Updating Room
        } catch (t: Throwable) {
            error.value = t.message  //Showing cached data if network fails
        } finally {
            loading.value = false    //Happening automatically via Flow
        }
    }
}
