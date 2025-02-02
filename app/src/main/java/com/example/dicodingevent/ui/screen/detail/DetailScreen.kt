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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    eventId: Int,
    onLoad: (name: String) -> Unit,
    context: Context = LocalContext.current,
    viewModel: DetailViewModel = viewModel()
) {
    val eventData by viewModel.event.collectAsState()
    val description = eventData?.description
    val isLoading = viewModel.isLoading
    val isConnectedToInternet = viewModel.isConnectedToInternet

    LaunchedEffect(eventId) {
        viewModel.getEventById(eventId.toString())
    }

    LaunchedEffect(eventData) {
        eventData?.let {
            onLoad(it.name.toString())
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (isConnectedToInternet && eventData != null) {
                GlideImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    model = eventData?.mediaCover,
                    contentDescription = "Event Image",
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = eventData?.name.toString(),
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = "By: " + eventData?.ownerName.toString(),
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = eventData?.summary.toString(),
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
                            text = "Sisa Kuota: " + (eventData?.quota!! - eventData?.registrants!!).toString(),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    val date = eventData?.beginTime.toString().trim()
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
                if (description != null) {
                    HtmlContent(description)
                } else {
                    Text(text = "Description not available")
                }
                Spacer(Modifier.height(4.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(eventData?.link.toString()))
                        context.startActivity(intent)
                    },
                    content = { Text("Register") }
                )
                Spacer(Modifier.height(16.dp))
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

@Composable
fun HtmlContent(html: String) {
    val context = LocalContext.current
    val annotatedText = parseHtmlToAnnotatedString(html)

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                    context.startActivity(intent)
                }
        },
        style = androidx.compose.ui.text.TextStyle(color = MaterialTheme.colorScheme.onBackground)
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