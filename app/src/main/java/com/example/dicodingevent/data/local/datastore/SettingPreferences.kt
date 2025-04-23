package com.example.dicodingevent.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

class SettingPreferences(val context: Context) {
    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
        private val REMINDER_KEY = booleanPreferencesKey("reminder_setting")
    }

    fun getThemeSetting(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        context.dataStore.edit {
            it[THEME_KEY] = isDarkModeActive
        }
    }

    fun getReminderSetting(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[REMINDER_KEY] ?: false
        }
    }

    suspend fun saveReminderSetting(isReminderActive: Boolean) {
        context.dataStore.edit {
            it[REMINDER_KEY] = isReminderActive
        }
    }
}