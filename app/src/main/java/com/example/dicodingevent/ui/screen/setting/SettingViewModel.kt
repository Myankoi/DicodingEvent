package com.example.dicodingevent.ui.screen.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.local.datastore.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel: ViewModel() {
    fun setTheme(context: Context, isActive: Boolean) {
        viewModelScope.launch {
            SettingPreferences(context).saveThemeSetting(isActive)
        }
    }
}