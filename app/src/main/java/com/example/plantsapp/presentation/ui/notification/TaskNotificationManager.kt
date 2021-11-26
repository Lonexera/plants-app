package com.example.plantsapp.presentation.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
    private var notificationId = 0
    private val pendingIntent = createPendingIntent()

    init {
        createChannel(context)
    }

    fun showTaskNotifications(
        plant: Plant,
        tasks: List<Task>
    ) {
        val notificationPicture = plant.plantPicture.getBitmapWithPlaceholder(context)

        val notifications = tasks.map {
            prepareTaskNotification(
                plantName = plant.name.value,
                plantPicture = notificationPicture,
                task = it
            )
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

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createCompletePendingIntent(notificationId: Int): PendingIntent {
        val completeIntent = Intent(context, NotificationBroadcastReceiver::class.java)
            .apply {
                putExtra(EXTRA_NOTIFICATION_ID, notificationId)
            }
        return PendingIntent.getBroadcast(context, 0, completeIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun prepareTaskNotification(
        plantName: String,
        plantPicture: Bitmap,
        task: Task
    ): Pair<Notification, Int> {
        val id = notificationId++

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_plant_24)
            .setContentTitle(plantName)
            .setContentText(
                context.getNotificationText(
                    plantName = plantName,
                    task = task
                )
            )
            .setLargeIcon(plantPicture)
            .setContentIntent(pendingIntent)
            .setGroup(plantName)
            .addAction(
                R.drawable.ic_complete,
                context.getString(R.string.title_notification_complete_button),
                createCompletePendingIntent(id)
            )
            .build() to id
    }

    private fun prepareSummaryNotification(
        plantName: String
    ): Notification {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_plant_24)
            .setContentIntent(pendingIntent)
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
            },
            plantName
        )
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "CHANNEL_TASK_NOTIFICATION_ID"
        private const val NOTIFICATION_REQUEST_CODE = 252
        private const val EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID"
    }
}
