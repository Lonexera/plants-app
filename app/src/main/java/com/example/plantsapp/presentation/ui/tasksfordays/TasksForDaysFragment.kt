package com.example.plantsapp.presentation.ui.tasksfordays

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plantsapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksForDaysFragment : Fragment(R.layout.fragment_tasks_for_days) {

    private val viewModel: TasksForDaysViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                TasksForDaysScreen(
                    viewModel = viewModel,
                    onOpenStatisticsApp = { intent -> startActivity(intent) }
                )
            }
        }
    }
}
