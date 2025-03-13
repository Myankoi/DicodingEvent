package com.example.dicodingevent.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import com.example.dicodingevent.ui.model.BottomBarItem

@Composable
fun bottomBarItem(): List<BottomBarItem> {
    return listOf(
        BottomBarItem(
            title = "Home",
            icon = Icons.Filled.Home
        ),
        BottomBarItem(
            title = "Upcoming",
            icon = Icons.Filled.DateRange
        ),
        BottomBarItem(
            title = "Finished",
            icon = Icons.Filled.CheckCircle
        ),
        BottomBarItem(
            title = "Favorite",
            icon = Icons.Filled.Favorite
        ),
        BottomBarItem(
            title = "Setting",
            icon = Icons.Filled.Settings
        )
    )
}