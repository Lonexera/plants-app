package com.example.plantsapp.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plantsapp.R
import com.example.plantsapp.databinding.ActivityMainBinding
import com.example.plantsapp.presentation.ui.plants.PlantsFragment
import com.example.plantsapp.presentation.ui.profile.ProfileFragment
import com.example.plantsapp.presentation.ui.tasksfordays.TasksForDaysFragment
import com.example.plantsapp.presentation.ui.tasksfordays.TasksForDaysScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private val binding: ActivityMainBinding by lazy {
//        ActivityMainBinding.inflate(layoutInflater)
//    }
//
//    // TODO maybe find a more proper way to handle bottom navigation visibility
//    private val bottomBarVisibilityCallback =
//        BottomBarVisibilityCallback { isBottomBarVisible ->
//            binding.bottomNavigation.isVisible = isBottomBarVisible
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TasksForDaysScreen(
                viewModel = hiltViewModel(),
                onOpenStatisticsApp = { intent -> startActivity(intent) }
            )
        }
//        setContentView(binding.root)
//        setUpHomeButtonOnAppBar()
//
//        supportFragmentManager.registerFragmentLifecycleCallbacks(
//            bottomBarVisibilityCallback,
//            false
//        )
//
//        // TODO set selected item on visible fragment
//        // can be visible Tasks but selected Profile - after signing out and signing in
//        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.bottom_nav_tasks -> openFragment(TasksForDaysFragment())
//                R.id.bottom_nav_plants -> openFragment(PlantsFragment())
//                R.id.bottom_nav_profile -> openFragment(ProfileFragment())
//            }
//            true
//        }
    }

    override fun onDestroy() {
        super.onDestroy()

//        supportFragmentManager.unregisterFragmentLifecycleCallbacks(
//            bottomBarVisibilityCallback
//        )
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                supportFragmentManager.popBackStack()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    private fun openFragment(fragment: Fragment) {
//        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        supportFragmentManager.commit {
//            replace(R.id.fragment_container, fragment)
//        }
//    }

//    private fun setUpHomeButtonOnAppBar() {
//        supportFragmentManager.addOnBackStackChangedListener {
//            val isHomeButtonVisible = supportFragmentManager.backStackEntryCount > 0
//
//            supportActionBar?.setDisplayHomeAsUpEnabled(isHomeButtonVisible)
//        }
//    }
}
