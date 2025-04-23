package com.example.dicodingevent.data.local.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.dicodingevent.R
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.ui.MainActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: EventRepository
) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val upcomingEvent = repository.getReminderEvent()
            if (upcomingEvent.isNullOrEmpty()) {
                return Result.success()
            }
            val latestEvent = upcomingEvent.firstOrNull()

            val formattedDate = latestEvent?.let {
                formatDate(it.beginTime.toString())
            }
            val eventId = latestEvent?.id ?: "Event Id"
            val eventName = latestEvent?.name ?: "Upcoming Event"
            val eventTime = formattedDate ?: "Check the app for details."
            createNotificationChannel()
            showNotification(eventId.toString(), eventName, eventTime)
            return Result.success()

        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val channelId = "event_reminder_channel"
            val channelName = "Event Reminder Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Notifikasi untuk event yang akan datang."
            }

            val notificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun formatDate(date: String): String {
        val date2 = date.replace(" ", "T")
        val parse = LocalDateTime.parse(date2)

        val formatterMonth = DateTimeFormatter.ofPattern("MMMM")
        val formatterDay = DateTimeFormatter.ofPattern("d")
        val day = parse.format(formatterDay).toInt()
        val month = parse.format(formatterMonth)

        val ordinal = when {
            day in 11..13 -> "th"
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }

        return "$month $day$ordinal"
    }

    private fun showNotification(eventId: String, eventName: String, eventTime: String) {
        val channelId = "event_reminder_channel"
        val notificationId = 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            putExtra("eventId", eventId)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            eventId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(eventName)
            .setContentText(eventTime)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(notificationId, notification)
    }
}