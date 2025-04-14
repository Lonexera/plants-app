package com.example.plantsapp.presentation.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.plantsapp.R
import com.example.plantsapp.presentation.ui.tasksfordays.TasksForDaysFragment
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

//    private val googleSignInLauncher =
//        registerForActivityResult(
//            GoogleSignInContract { googleSignInClient }
//        ) { token ->
//            viewModel.onSignInResult(token)
//        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                AuthScreen(
                    viewModel = viewModel,
                    onNavigateToTasks = {
                        requireActivity().supportFragmentManager.commit {
                            replace(R.id.fragment_container, TasksForDaysFragment())
                        }
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}
