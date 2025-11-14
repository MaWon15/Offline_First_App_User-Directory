package com.example.userdirectory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.userdirectory.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val vm: com.example.userdirectory.ui.UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                com.example.userdirectory.ui.screen.UsersListScreen(
                    state = vm.state,
                    onQuery = vm::setQuery,
                    onRefresh = vm::refresh
                    )
                }
            }
        }
    }