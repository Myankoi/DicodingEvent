package com.example.dicodingevent.ui.screen.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.local.datastore.SettingPreferences
import com.example.dicodingevent.data.local.workmanager.ReminderWorker
import com.example.dicodingevent.data.remote.response.EventsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class SettingViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _event = MutableStateFlow<Result<List<EventsItem?>?>>(Result.Loading)
    val event: StateFlow<Result<List<EventsItem?>?>> = _event

    init {
        viewModelScope.launch {
            try {
                val response = eventRepository.getReminderEvent()
                if (response != null) {
                    _event.value = Result.Success(response)
                } else {
                    _event.value = Result.Error("Event not found")
                }
            } catch (e: Exception) {
                Log.e("SettingViewModel", "Error fetching event", e)
                _event.value = Result.Error("No Internet")
            }
        }
    }

    fun setTheme(context: Context, isActive: Boolean) {
        viewModelScope.launch {
            SettingPreferences(context).saveThemeSetting(isActive)
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

    fun setReminder(context: Context, isActive: Boolean) {
        viewModelScope.launch {
            SettingPreferences(context).saveReminderSetting(isActive)
        }
    }

    fun setReminderNotification(
        context: Context,
        isEnabled: Boolean,
        eventId: String?,
        eventName: String?,
        eventTime: String?
    ) {
        val workManager = WorkManager.getInstance(context)

        if (isEnabled && eventId != null) {
            val formattedDate = eventTime?.let {
                formatDate(it)
            }
            val data = workDataOf(
                "event_id" to eventId,
                "event_name" to eventName,
                "event_time" to "Don't miss the event on $formattedDate! Check the details now."
            )

            val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                .setInputData(data)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "DailyReminder",
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )

            /*val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInputData(data)
                .build()
            workManager.enqueue(workRequest)*/

        } else {
            workManager.cancelUniqueWork("DailyReminder")
        }
    }
}