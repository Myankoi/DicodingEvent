package com.example.dicodingevent.ui.screen.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.local.datastore.SettingPreferences
import com.example.dicodingevent.data.local.workmanager.ReminderWorker
import com.example.dicodingevent.data.remote.response.EventsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _event = MutableStateFlow<Result<List<EventsItem?>?>>(Result.Loading)
    val event: StateFlow<Result<List<EventsItem?>?>> = _event

    fun setTheme(context: Context, isActive: Boolean) {
        viewModelScope.launch {
            SettingPreferences(context).saveThemeSetting(isActive)
        }
    }

    fun setReminder(context: Context, isActive: Boolean) {
        viewModelScope.launch {
            SettingPreferences(context).saveReminderSetting(isActive)
        }
    }

    fun setReminderNotification(
        context: Context,
        isEnabled: Boolean,
    ) {
        val workManager = WorkManager.getInstance(context)

        if (isEnabled) {
            workManager.cancelUniqueWork("DailyReminder")

            val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "DailyReminder",
                ExistingPeriodicWorkPolicy.REPLACE,
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