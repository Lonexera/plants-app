package com.example.plantsapp.presentation.ui.tasksfordays

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantsapp.R
import com.example.plantsapp.presentation.ui.tasks.TasksScreen
import com.example.plantsapp.presentation.ui.tasks.TasksViewModel
import com.example.plantsapp.presentation.ui.utils.formatDate
import com.example.plantsapp.presentation.ui.utils.plusDays
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksForDaysScreen(
    viewModel: TasksForDaysViewModel,
    onOpenStatisticsApp: (Intent) -> Unit,
) {
    val todayDate by viewModel.todayDate.collectAsStateWithLifecycle()
    val isStatisticsAppInstalled by viewModel.isStatisticsAppInstalled.collectAsStateWithLifecycle()
    var currentPage by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.launchStatisticsApp.collect { event ->
            event.getContentIfNotHandled()?.let { intent ->
                onOpenStatisticsApp(intent)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_tasks_screen)) },
                actions = {
                    if (isStatisticsAppInstalled) {
                        IconButton(onClick = { viewModel.onStatisticsClicked() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_statistics_24),
                                contentDescription = "Statistics"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Date Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentPage > 0) {
                    TextButton(
                        onClick = { currentPage-- }
                    ) {
                        Text(getDateText(todayDate, currentPage - 1))
                    }
                } else {
                    Spacer(modifier = Modifier.width(80.dp))
                }

                Text(
                    text = getDateText(todayDate, currentPage, true),
                    style = MaterialTheme.typography.titleMedium
                )

                TextButton(
                    onClick = { currentPage++ }
                ) {
                    Text(getDateText(todayDate, currentPage + 1))
                }
            }

            // Tasks Screen
            TasksScreen(
                viewModel = TasksViewModel.provideHiltViewModel(
                    date = todayDate.plusDays(currentPage)
                )
            )
        }
    }
}

private const val DATE_FORMAT_WITH_YEAR = "d MMM yyyy"
private const val DATE_FORMAT_WITHOUT_YEAR = "d MMM"

@Composable
private fun getDateText(
    date: Date,
    offset: Int,
    showWithYear: Boolean = false
): String {
    return when (offset) {
        0 -> stringResource(R.string.title_today_date)
        1 -> stringResource(R.string.title_tomorrow_date)
        else -> {
            val format = if (showWithYear) {
                DATE_FORMAT_WITH_YEAR
            } else {
                DATE_FORMAT_WITHOUT_YEAR
            }
            date.plusDays(offset).formatDate(format)
        }
    }
}
