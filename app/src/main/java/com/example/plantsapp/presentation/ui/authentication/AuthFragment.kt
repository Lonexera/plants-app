package com.example.plantsapp.presentation.ui.authentication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plantsapp.R
import com.example.plantsapp.databinding.FragmentAuthBinding
import com.example.plantsapp.presentation.ui.loading.LoadingDialog
import com.example.plantsapp.presentation.ui.tasksfordays.TasksForDaysFragment
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {
    private val binding: FragmentAuthBinding by viewBinding(FragmentAuthBinding::bind)
    private val viewModel: AuthViewModel by viewModels()
    @Inject
    lateinit var googleSignInClient: GoogleSignInClient
    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    private val googleSignInLauncher =
        registerForActivityResult(
            GoogleSignInContract { googleSignInClient }
        ) { token ->
            viewModel.onSignInResult(token)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setTitle(R.string.title_auth_screen)

        with(viewModel) {
            authState.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { result ->
                    when (result) {
                        is AuthViewModel.AuthState.Loading -> loadingDialog.show()

                        is AuthViewModel.AuthState.NavigateToTasks -> {
                            loadingDialog.dismiss()
                            requireActivity().supportFragmentManager.commit {
                                replace(R.id.fragment_container, TasksForDaysFragment())
                            }
                        }
                        is AuthViewModel.AuthState.AuthError -> {
                            loadingDialog.dismiss()
                            Toast.makeText(requireContext(), result.errorId, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        with(binding) {
            btnGoogleSignIn.setOnClickListener {
                googleSignInLauncher.launch(null)
            }
        }
    }
}
