package com.example.plantsapp.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.plantsapp.R
import com.example.plantsapp.presentation.ui.navigation.AppDestination
import com.example.plantsapp.presentation.ui.navigation.PlantsAppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            var selectedBarItem by remember { mutableStateOf(PlantsAppNavigationBarItem.Tasks) }

            Column {
                PlantsAppNavGraph(
                    modifier = Modifier.weight(1f),
                    navController = navController,
                    onOpenStatisticsApp = { intent -> startActivity(intent) }
                )

                NavigationBar {
                    PlantsAppNavigationBarItem.entries.forEach { item ->
                        NavigationBarItem(
                            selected = selectedBarItem == item,
                            icon = { item.Icon() },
                            label = { item.Label() },
                            onClick = {
                                when (item) {
                                    PlantsAppNavigationBarItem.Tasks -> navController.navigate(AppDestination.Tasks)
                                    PlantsAppNavigationBarItem.MyPlants -> navController.navigate(AppDestination.MyPlants)
                                    PlantsAppNavigationBarItem.Profile -> navController.navigate(AppDestination.Profile)
                                }
                                selectedBarItem = item
                            },
                        )
                    }
                }
            }
        }
    }
}

enum class PlantsAppNavigationBarItem {
    Tasks,
    MyPlants,
    Profile,
    ;

    @Composable
    fun Icon() {
        when (this) {
            Tasks -> {
                Icon(
                    painter = painterResource(R.drawable.ic_home_24),
                    contentDescription = "Home"
                )
            }
            MyPlants -> {
                Icon(
                    painter = painterResource(R.drawable.ic_plant_24),
                    contentDescription = "Plant"
                )
            }
            Profile -> {
                Icon(
                    painter = painterResource(R.drawable.ic_profile_24),
                    contentDescription = "Profile"
                )
            }
        }
    }

    @Composable
    fun Label() {
        Text(
            text = stringResource(
                id = when (this) {
                    Tasks -> R.string.title_bottom_nav_tasks
                    MyPlants -> R.string.title_bottom_nav_plants
                    Profile -> R.string.title_bottom_nav_profile
                }
            )
        )
    }
}
