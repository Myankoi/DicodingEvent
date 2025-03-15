package com.example.dicodingevent.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowInsetsControllerCompat
import com.example.dicodingevent.data.local.datastore.SettingPreferences
import com.example.dicodingevent.navigation.AppNavigation
import com.example.dicodingevent.ui.theme.DicodingEventTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkModeActive by SettingPreferences(this).getThemeSetting().collectAsState(false)
            DicodingEventTheme(dynamicColor = false, darkTheme = isDarkModeActive) {
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
                AppNavigation(isDarkModeActive = isDarkModeActive)
            }
        }
    }
}
