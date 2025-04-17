package com.example.plantsapp.presentation.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plantsapp.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject

// TODO - open camera on creation of task screen
@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks) {
    @Inject
    lateinit var assistedFactory: TasksViewModelAssistedFactory
    private val tasksViewModel: TasksViewModel by viewModels {
        TasksViewModelFactory(
            assistedFactory = assistedFactory,
            date = requireArguments().date
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                TasksScreen(viewModel = tasksViewModel)
            }
        }
    }

    companion object {
        private const val ARGUMENT_DATE = "ARGUMENT_DATE"
        private var Bundle.date: Date
            get() = Date(
                getLong(ARGUMENT_DATE)
            )
            set(date) {
                putLong(ARGUMENT_DATE, date.time)
            }

        fun newInstance(date: Date): TasksFragment {
            val fragment = TasksFragment()
            fragment.arguments = bundleOf().apply {
                this.date = date
            }
            return fragment
        }
    }
}
