package com.example.dicodingevent.ui.screen.upcoming

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.ui.component.VerticalEventCard
import com.example.dicodingevent.ui.factory.EventViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingScreen(
    modifier: Modifier,
    onClickEvent: (id: String) -> Unit,
    eventRepository: EventRepository,
    viewModel: UpcomingViewModel = viewModel(factory = EventViewModelFactory(eventRepository))
) {
    val upcomingEvents by viewModel.upcomingEvents.collectAsState()
    var search by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        viewModel.getEvents()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        SearchBar(
            windowInsets = WindowInsets(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            query = search,
            onQueryChange = {
                search = it
                viewModel.getEvents(search = it)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                )
            },
            placeholder = { Text("Search") },
            active = false,
            onActiveChange = {},
            onSearch = {},
            content = {}
        )
        when (upcomingEvents) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            is Result.Success -> {
                val upcomingEventData = (upcomingEvents as Result.Success).data
                if (upcomingEventData.isEmpty()) {
                    Text(
                        "Events Not Found",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(upcomingEventData) { event ->
                            event?.let {
                                VerticalEventCard(
                                    modifier = Modifier,
                                    event = it,
                                    image = 2,
                                    onClickEvent = { onClickEvent(it.id.toString()) }
                                )
                            }
                        }
                    }
                }
            }

            is Result.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (upcomingEvents as Result.Error).error,
                        color = Color.Red
                    )
                }
            }
        }
    }
}