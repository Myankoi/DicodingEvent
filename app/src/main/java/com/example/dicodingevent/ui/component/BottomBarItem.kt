package com.example.dicodingevent.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
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
        )
    )
}