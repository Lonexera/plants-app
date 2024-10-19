package com.example.plantsapp.presentation.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.plantsapp.presentation.ui.authentication.AuthFragment

/** Gets current visible fragment and if it is one of
 * Authentication fragments ([AuthFragment] or [zzc]) returns false
 * (as Bottom Navigation Bar should not be visible).
 */
class BottomBarVisibilityCallback(
    private val isBottomBarVisible: (Boolean) -> Unit
) : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)

        val currentVisibleFragment = fm.fragments.last()
        isBottomBarVisible(
            !(currentVisibleFragment is AuthFragment)
        )
    }
}
