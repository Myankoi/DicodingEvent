package com.example.dicodingevent.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.ui.component.HorizontalEventCard
import com.example.dicodingevent.ui.component.VerticalEventCard
import com.example.dicodingevent.ui.factory.EventViewModelFactory

@Composable
fun HomeScreen(
    modifier: Modifier,
    onClickEvent: (id: String) -> Unit,
    eventRepository: EventRepository,
    viewModel: HomeViewModel = viewModel(factory = EventViewModelFactory(eventRepository)),
) {
    LaunchedEffect(true) {
        viewModel.getEvents()
    }

    val upcomingEvents by viewModel.upcomingEvents.collectAsState()
    val finishedEvents by viewModel.finishedEvents.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        when {
            (upcomingEvents is Result.Loading) && (finishedEvents is Result.Loading) -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            (upcomingEvents is Result.Success) && (finishedEvents is Result.Success) -> {
                val upcomingEventData = (upcomingEvents as Result.Success).data
                val finishedEventData = (finishedEvents as Result.Success).data
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    text = if (upcomingEventData.isEmpty()) "Stay tuned for upcoming events!" else "Upcoming Events"
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(upcomingEventData) { event ->
                        event?.let {
                            VerticalEventCard(
                                modifier = Modifier.width(150.dp),
                                event = it,
                                image = 1,
                                onClickEvent = { onClickEvent(it.id.toString()) }
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    text = "Finished Events"
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(finishedEventData) { event ->
                        event?.let {
                            HorizontalEventCard(
                                modifier = Modifier,
                                event = it,
                                onClickEvent = { onClickEvent(it.id.toString()) }
                            )
                        }
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No Internet Connection",
                        color = Color.Red
                    )
                }
            }
        }
    }
}