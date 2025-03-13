package com.example.dicodingevent.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.dicodingevent.data.remote.response.EventsItem

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VerticalEventCard(
    modifier: Modifier,
    event: EventsItem,
    image: Int,
    onClickEvent: () -> Unit,
) {
    OutlinedCard(
        modifier = modifier.clickable { onClickEvent() },
        colors = CardDefaults.outlinedCardColors(MaterialTheme.colorScheme.background),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            GlideImage(
                modifier = modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                model = if (image == 1) event.imageLogo else event.mediaCover,
                contentScale = ContentScale.FillWidth,
                contentDescription = "Event Image",
            )
            Text(
                modifier = Modifier.padding(8.dp),
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                maxLines = if (image == 1) 1 else 2,
                text = event.name ?: "Unnamed Event",
            )
        }
    }
}
