package com.example.dicodingevent.ui.screen.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Spanned
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.FavoriteEventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.ui.factory.DetailViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    eventId: Int,
    onLoad: (name: String) -> Unit,
    context: Context = LocalContext.current,
    eventRepository: EventRepository,
    favEventRepository: FavoriteEventRepository,
    viewModel: DetailViewModel = viewModel(
        factory = DetailViewModelFactory(
            eventRepository,
            favEventRepository
        )
    )
) {
    val eventData by viewModel.event.collectAsState()
    val isAlreadyFav by viewModel.isExist.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.getEventById(eventId.toString())
        viewModel.checkIfExist(eventId.toString())
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (eventData) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Result.Success -> {
                val event = (eventData as Result.Success).data
                event.name?.let { onLoad(it) }
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GlideImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(8.dp)),
                        model = event.mediaCover,
                        contentScale = ContentScale.FillWidth,
                        contentDescription = "Event Image",
                    )
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-16).dp, y = 16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        onClick = {
                            viewModel.addToFavRepository(
                                event.id.toString(),
                                event.name.toString(),
                                event.mediaCover.toString()
                            )
                        },
                        content = {
                            Icon(
                                imageVector = if (!isAlreadyFav) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite,
                                tint = if (!isAlreadyFav) MaterialTheme.colorScheme.background else Color.Red,
                                contentDescription = null
                            )
                        }
                    )
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = event.name.toString(),
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = "By: " + event.ownerName.toString(),
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = event.summary.toString(),
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(8.dp)
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            text = "Sisa Kuota: " + (event.quota!! - event.registrants!!).toString(),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    val date = event.beginTime.toString().trim()
                    val date2 = date.replace(" ", "T")
                    val parse = LocalDateTime.parse(date2)
                    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")
                    val formattedDate = parse.format(formatter)
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(8.dp)
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            text = formattedDate,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                if (event.description != null) {
                    HtmlContent(event.description)
                } else {
                    Text(text = "Description not available")
                }
                Spacer(Modifier.height(4.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    onClick = {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(event.link.toString()))
                        context.startActivity(intent)
                    },
                    content = { Text("Register") }
                )
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

@Composable
fun HtmlContent(html: String) {
    val context = LocalContext.current
    val annotatedText = parseHtmlToAnnotatedString(html)

    @Suppress("DEPRECATION")
    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                    context.startActivity(intent)
                }
        },
        style = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Justify
        )
    )
}

fun parseHtmlToAnnotatedString(html: String): AnnotatedString {
    val spanned: Spanned = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    return buildAnnotatedString {
        var cursor = 0
        spanned.getSpans(0, spanned.length, android.text.style.URLSpan::class.java).forEach {
            val start = spanned.getSpanStart(it)
            val end = spanned.getSpanEnd(it)

            if (start > cursor) {
                append(spanned.subSequence(cursor, start).toString())
            }

            pushStringAnnotation(tag = "URL", annotation = it.url)
            withStyle(style = SpanStyle(color = Color.Blue)) {
                append(spanned.subSequence(start, end).toString())
            }
            pop()
            cursor = end
        }

        if (cursor < spanned.length) {
            append(spanned.subSequence(cursor, spanned.length).toString())
        }
    }
}