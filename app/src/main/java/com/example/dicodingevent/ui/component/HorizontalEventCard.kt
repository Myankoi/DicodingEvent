package com.example.dicodingevent.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.dicodingevent.data.remote.response.EventsItem

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HorizontalEventCard(
    modifier: Modifier,
    event: EventsItem,
    onClickEvent: () -> Unit,
) {
    OutlinedCard(
        modifier = modifier.clickable { onClickEvent() },
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth().padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(shape = CardDefaults.shape),
                    model = event.imageLogo,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "Event Image",
                )
                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        text = event.name ?: "Unnamed Event",
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = event.summary ?: "Unnamed Event",
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Justify,
                        maxLines = 3,
                    )
                }
            }
        }
    }
}