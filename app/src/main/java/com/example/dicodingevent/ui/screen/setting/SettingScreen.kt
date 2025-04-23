    package com.example.dicodingevent.ui.screen.setting

    import android.content.Context
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.HorizontalDivider
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Switch
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.example.dicodingevent.data.EventRepository
    import com.example.dicodingevent.data.Result
    import com.example.dicodingevent.ui.factory.EventViewModelFactory

    @Composable
    fun SettingScreen(
        modifier: Modifier,
        context: Context = LocalContext.current,
        isDarkModeActive: Boolean,
        isReminderActive: Boolean,
        eventRepository: EventRepository,
        viewModel: SettingViewModel = viewModel(factory = EventViewModelFactory(eventRepository))
    ) {
        val upComingEvent by viewModel.event.collectAsState()
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Dark Mode",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Enable dark mode",
                        fontSize = 12.sp,
                    )
                }
                Switch(
                    checked = isDarkModeActive,
                    onCheckedChange = { isChecked ->
                        viewModel.setTheme(context, isChecked)
                    }
                )
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Daily Reminder",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Get daily reminders about upcoming events",
                        fontSize = 12.sp,
                    )
                }
                if ((upComingEvent is Result.Loading)) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    val event = when (upComingEvent) {
                        is Result.Success -> (upComingEvent as Result.Success).data?.firstOrNull()
                        else -> null
                    }
                    Switch(
                        checked = isReminderActive,
                        onCheckedChange = { isChecked ->
                            viewModel.setReminder(context, isChecked)
                            viewModel.setReminderNotification(
                                context,
                                isChecked
                            )
                        }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
        }
    }