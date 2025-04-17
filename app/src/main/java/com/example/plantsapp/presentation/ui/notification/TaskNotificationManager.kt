package com.example.plantsapp.presentation.ui.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.plantsapp.R
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.Task
import com.example.plantsapp.presentation.ui.MainActivity
import com.example.plantsapp.presentation.ui.notification.broadcastreceiver.NotificationBroadcastReceiver
import com.example.plantsapp.presentation.ui.utils.getBitmapWithPlaceholder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TaskNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // TODO make this depend on task it needs to show
    private var notificationId = 0
    private val clickPendingIntent by lazy {
        PendingIntent.getActivity(
            context,
            NOTIFICATION_REQUEST_CODE,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
    }
    private val notificationManager get() =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createChannel(context)
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showTaskNotifications(
        plant: Plant,
        tasks: List<Task>
    ) {
        val notificationPicture = plant.plantPicture
            ?.toUri()
            .getBitmapWithPlaceholder(context)

        val notifications = tasks.map {
            prepareTaskNotification(
                plantName = plant.name,
                plantPicture = notificationPicture,
                task = it,
                notificationId = notificationId
            ) to notificationId++
        }

        val summaryNotification = prepareSummaryNotification(plant.name.value)

        notifications.forEach { (notification, id) ->
            NotificationManagerCompat.from(context).notify(id, notification)
        }
        NotificationManagerCompat.from(context).notify(notificationId++, summaryNotification)
    }

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.title_notification_channel)
            val description = context.getString(R.string.msg_notification_description)
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = description

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun prepareTaskNotification(
        plantName: Plant.Name,
        plantPicture: Bitmap,
        task: Task,
        notificationId: Int
    ): Notification {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_plant_24)
            .setContentTitle(plantName.value)
            .setContentText(
                context.getNotificationText(
                    plantName = plantName.value,
                    task = task
                )
            )
            .setLargeIcon(plantPicture)
            .setContentIntent(clickPendingIntent)
            .setGroup(plantName.value)
            .addAction(
                R.drawable.ic_complete,
                context.getString(R.string.title_notification_complete_button),
                context.getPendingIntentForTask(
                    task = task,
                    notificationId = notificationId,
                    requestCode = notificationId,
                    plantName = plantName
                )
            )
            .build()
    }

    private fun prepareSummaryNotification(
        plantName: String
    ): Notification {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_plant_24)
            .setContentIntent(clickPendingIntent)
            .setGroup(plantName)
            .setGroupSummary(true)
            .build()
    }

    private fun Context.getNotificationText(plantName: String, task: Task): String {
        return getString(
            when (task) {
                is Task.WateringTask -> R.string.msg_notification_task_watering
                is Task.SprayingTask -> R.string.msg_notification_task_spraying
                is Task.LooseningTask -> R.string.msg_notification_task_loosening
                is Task.TakingPhotoTask -> R.string.msg_notification_task_taking_photo
            },
            plantName
        )
    }

    private fun Context.getPendingIntentForTask(
        task: Task,
        notificationId: Int,
        requestCode: Int,
        plantName: Plant.Name
    ): PendingIntent {
        return when (task) {
            is Task.TakingPhotoTask -> clickPendingIntent
            else -> {
                NotificationBroadcastReceiver.createCompletePendingIntent(
                    context = this,
                    task = task,
                    notificationId = notificationId,
                    requestCode = requestCode,
                    plantName = plantName
                )
            }
        }
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "CHANNEL_TASK_NOTIFICATION_ID"
        private const val NOTIFICATION_REQUEST_CODE = 252
    }
}
