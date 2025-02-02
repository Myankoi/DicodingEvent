package com.example.dicodingevent.ui.screen.finished

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.dicodingevent.ui.component.VerticalEventCard
import com.example.dicodingevent.ui.screen.upcoming.UpcomingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingScreen(
    modifier: Modifier,
    onClickEvent: (id: String) -> Unit,
    viewModel: UpcomingViewModel = viewModel()
) {
    val upcomingEvents by viewModel.upcomingEvents.collectAsState()
    val isLoading = viewModel.isLoading
    val isConnectedToInternet = viewModel.isConnectedToInternet
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
                    contentDescription = null
                )
            },
            placeholder = { Text("Search") },
            active = false,
            onActiveChange = {},
            onSearch = {},
            content = {}
        )
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            if (isConnectedToInternet && upcomingEvents != null) {
                if (upcomingEvents.isNullOrEmpty()) {
                    Text(
                        "Events tidak ditemukan.",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        upcomingEvents?.let { events ->
                            items(events) { event ->
                                event?.let {
                                    VerticalEventCard(
                                        modifier = Modifier,
                                        event = it,
                                        image = 0,
                                        onClickEvent = { onClickEvent(it.id.toString()) }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isConnectedToInternet) {
                        Text(
                            text = "Tidak ada koneksi internet.",
                            color = Color.Red
                        )
                    } else {
                        Text(
                            text = "Error fetching events.",
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}