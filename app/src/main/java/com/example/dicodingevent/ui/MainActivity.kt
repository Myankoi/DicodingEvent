package com.example.dicodingevent.ui

import android.os.Bundle
import android.view.WindowContentFrameStats
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowInsetsControllerCompat
import com.example.dicodingevent.navigation.AppNavigation
import com.example.dicodingevent.ui.theme.DicodingEventTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DicodingEventTheme(dynamicColor = false) {
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
                AppNavigation()
            }
        }
    }
}
