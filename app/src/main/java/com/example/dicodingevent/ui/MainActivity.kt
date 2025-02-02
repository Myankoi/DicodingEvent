package com.example.dicodingevent.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dicodingevent.navigation.AppNavigation
import com.example.dicodingevent.ui.theme.DicodingEventTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DicodingEventTheme(dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}
