package com.example.plantsapp.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.plantsapp.R
import com.example.plantsapp.presentation.ui.authentication.AuthFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToAuth = {
                        requireActivity().supportFragmentManager.commit {
                            replace(R.id.fragment_container, AuthFragment())
                        }
                    }
                )
            }
        }
    }
}
