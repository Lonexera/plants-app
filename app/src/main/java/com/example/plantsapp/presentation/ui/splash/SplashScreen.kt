package com.example.plantsapp.presentation.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToAuth: () -> Unit,
    onNavigateToTasks: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.navigate.collect { navigationEvent ->
            navigationEvent.getContentIfNotHandled()?.let { direction ->
                when (direction) {
                    SplashViewModel.Directions.AUTH_SCREEN -> onNavigateToAuth()
                    SplashViewModel.Directions.TASKS_SCREEN -> onNavigateToTasks()
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // change to logo
        CircularProgressIndicator()
    }
} 