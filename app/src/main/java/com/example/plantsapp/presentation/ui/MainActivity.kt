package com.example.plantsapp.presentation.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.plantsapp.R
import com.example.plantsapp.databinding.ActivityMainBinding
import com.example.plantsapp.presentation.ui.notification.NotificationWorker
import com.example.plantsapp.presentation.ui.plants.PlantsFragment
import com.example.plantsapp.presentation.ui.tasksfordays.TasksForDaysFragment
import com.example.plantsapp.presentation.ui.utils.calculateDelay
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_nav_tasks -> openFragment(TasksForDaysFragment())
                R.id.bottom_nav_plants -> openFragment(PlantsFragment())
            }
            true
        }

        startNotificationWorker(
            context = applicationContext,
            startDate = Calendar.getInstance()
        )
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
        }
    }

    private fun startNotificationWorker(context: Context, startDate: Calendar) {
        val notificationRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(
                    startDate.calculateDelay(nextDayHour = HOUR_OF_WORK_STARTING),
                    TimeUnit.MILLISECONDS
                )
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            NotificationWorker.NOTIFICATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            notificationRequest
        )
    }

    companion object {
        private const val HOUR_OF_WORK_STARTING = 10
    }
}
