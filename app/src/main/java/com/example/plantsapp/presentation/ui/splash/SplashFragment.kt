package com.example.plantsapp.presentation.ui.splash

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.plantsapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(), LifecycleObserver {

    private val viewModel: SplashViewModel by viewModels()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        with(viewModel) {
            navigate.observe(this@SplashFragment) {
                it.getContentIfNotHandled()?.let { direction ->
                    when (direction) {
                        SplashViewModel.Directions.AUTH_SCREEN -> openFragment(AuthFragment())
                        SplashViewModel.Directions.TASKS_SCREEN -> openFragment(TasksForDaysFragment())
                    }
                }
            }
        }
        activity?.lifecycle?.removeObserver(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)
    }

    private fun openFragment(fragment: Fragment) {
//        requireActivity().supportFragmentManager.commit {
//            replace(R.id.fragment_container, fragment)
//        }
    }
}
