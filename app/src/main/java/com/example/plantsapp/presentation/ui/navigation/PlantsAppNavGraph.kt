package com.example.plantsapp.presentation.ui.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.presentation.ui.authentication.AuthScreen
import com.example.plantsapp.presentation.ui.plantcreation.PlantCreationScreen
import com.example.plantsapp.presentation.ui.plantdetail.PlantDetailScreen
import com.example.plantsapp.presentation.ui.plantdetail.PlantDetailViewModel
import com.example.plantsapp.presentation.ui.plants.PlantsScreen
import com.example.plantsapp.presentation.ui.profile.ProfileScreen
import com.example.plantsapp.presentation.ui.splash.SplashScreen
import com.example.plantsapp.presentation.ui.tasksfordays.TasksForDaysScreen
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

object AppDestination {
    @Serializable
    data object Splash

    @Serializable
    data object Authentication

    @Serializable
    data object Tasks

    @Serializable
    data object MyPlants

    @Serializable
    data object PlantCreation

    @Serializable
    data class PlantDetail(@Contextual val plantName: Plant.Name)

    @Serializable
    data object Profile
}

@Composable
fun PlantsAppNavGraph(
    navController: NavHostController,
    onOpenStatisticsApp: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppDestination.Splash
    ) {
        composable<AppDestination.Splash> {
            SplashScreen(
                viewModel = hiltViewModel(),
                onNavigateToAuth = { navController.navigate(AppDestination.Authentication) },
                onNavigateToTasks = { navController.navigate(AppDestination.Tasks) }
            )
        }

        composable<AppDestination.Authentication> {
            AuthScreen(
                viewModel = hiltViewModel(),
                onNavigateToTasks = { navController.navigate(AppDestination.Tasks) }
            )
        }

        composable<AppDestination.Tasks> {
            TasksForDaysScreen(
                viewModel = hiltViewModel(),
                onOpenStatisticsApp = onOpenStatisticsApp,
            )
        }

        composable<AppDestination.MyPlants> {
            PlantsScreen(
                viewModel = hiltViewModel(),
                onNavigateToPlantDetail = { navController.navigate(AppDestination.PlantDetail(it.name)) },
                onNavigateToPlantCreation = { navController.navigate(AppDestination.PlantCreation)}
            )
        }

        composable<AppDestination.PlantCreation> {
            PlantCreationScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable<AppDestination.PlantDetail> { entry ->
            val input = entry.toRoute<AppDestination.PlantDetail>()

            PlantDetailScreen(
                viewModel = PlantDetailViewModel.provideHiltViewModel(plantName = input.plantName),
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable<AppDestination.Profile> {
            ProfileScreen(
                // assisted
                viewModel = hiltViewModel(),
                onNavigateToAuth = { navController.navigate(AppDestination.Authentication) },
            )
        }
    }
}