package com.example.dicodingevent.ui.screen.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.dicodingevent.data.FavoriteEventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.ui.component.VerticalEventCard
import com.example.dicodingevent.ui.factory.FavoriteViewModelFactory

@Composable
fun FavoriteScreen(
    modifier: Modifier,
    onClickEvent: (id: String) -> Unit,
    favEventRepository: FavoriteEventRepository,
    viewModel: FavoriteViewModel = viewModel(factory = FavoriteViewModelFactory(favEventRepository))
) {
    val favEvents by viewModel.favEvents.collectAsState()

    LaunchedEffect(true) {
        viewModel.getEvents()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when(favEvents) {
            is Result.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            is Result.Success -> {
                val favEventData = (favEvents as Result.Success).data
                if (favEventData.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "You don't have any favorite events"
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(favEventData) { event ->
                            VerticalEventCard(
                                modifier = Modifier,
                                favEvent = event,
                                image = 2,
                                onClickEvent = { onClickEvent(event.id) }
                            )
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
                        text = "No Internet Connection.",
                        color = Color.Red
                    )
                }
            }
        }
    }
}