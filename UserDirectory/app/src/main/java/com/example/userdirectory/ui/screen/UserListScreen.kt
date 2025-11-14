package com.example.userdirectory.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.ui.UiState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen(
    state: StateFlow<UiState>,
    onQuery: (String) -> Unit,
    onRefresh: () -> Unit
) {
    val ui = state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Directory") },
                actions = { TextButton(onClick = onRefresh) { Text("Refresh") } }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = ui.value.query,
                onValueChange = onQuery,
                label = { Text("Search name or email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            Box(Modifier.fillMaxSize()) {
                when {
                    ui.value.isLoading && ui.value.users.isEmpty() ->
                        CircularProgressIndicator(Modifier.align(Alignment.Center))

                    ui.value.error != null && ui.value.users.isEmpty() ->
                        Column(
                            Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Error: ${ui.value.error}")
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = onRefresh) { Text("Try Again") }
                        }

                    else ->
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(ui.value.users) { u ->
                                UserRow(u)
                            }
                        }
                }
            }
        }
    }
}

@Composable
private fun UserRow(user: UserEntity) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // 3.3 Display required fields: id, name, email, phone
            Text("ID: ${user.id}", style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.height(4.dp))
            Text(user.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(2.dp))
            Text(user.email, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            user.phone?.let {
                Spacer(Modifier.height(2.dp))
                Text(it, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
