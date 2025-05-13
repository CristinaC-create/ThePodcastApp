
package com.cristinacabral.thepodcastapp.ui

import android.content.Intent
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter

// Updated Podcast model with audioUrl
data class Podcast(
    val title: String,
    val description: String,
    val imageUrl: String,
    val websiteUrl: String,
    val category: String,
    val audioUrl: String
)

val samplePodcasts = listOf(
    Podcast(
        "Planet Money",
        "The economy explained...",
        "https://media.npr.org/assets/img/2021/07/06/planet-money_sq-187379f9759d0030f7c09c6ce1c5a92edaa9d31d.jpg",
        "https://www.npr.org/sections/money/",
        "Finance",
        "https://www.npr.org/2023/07/21/1189028595/why-gas-prices-are-high.mp3"
    ),
    Podcast(
        "Science Vs",
        "Science Vs myths and fads...",
        "https://images.megaphone.fm/sVm5cdOjEpYWSNkFdqbs8vqw7iG1Q0RA0DWU1WXvbh4/plain/s3://megaphone-prod/podcasts/image.jpg",
        "https://gimletmedia.com/shows/science-vs",
        "Science",
        "https://www.npr.org/2023/07/07/1186701633/are-vitamins-a-scam.mp3"
    )
)

@Composable
fun PodcastListScreen() {
    LocalContext.current
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All") + samplePodcasts.map { it.category }.distinct()
    val filtered = samplePodcasts.filter {
        (selectedCategory == "All" || it.category == selectedCategory) &&
                it.title.contains(searchQuery.text, ignoreCase = true)
    }

    Column(Modifier.padding(16.dp)) {
        Text("\uD83C\uDFA7 Discover Podcasts", fontSize = 26.sp, color = Color(0xFF0288D1))

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filtered) { podcast ->
                PodcastCard(podcast)
            }
        }
    }
}

@Composable
fun PodcastCard(podcast: Podcast) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }
    var isPlaying by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Image(
                painter = rememberAsyncImagePainter(podcast.imageUrl),
                contentDescription = podcast.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(podcast.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                podcast.description,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, podcast.websiteUrl.toUri())
                    context.startActivity(intent)
                }) {
                    Text("Visit Website")
                }
                Button(onClick = {
                    try {
                        if (!isPlaying) {
                            mediaPlayer.reset()
                            mediaPlayer.setDataSource(podcast.audioUrl)
                            mediaPlayer.prepare()
                            mediaPlayer.start()
                            isPlaying = true
                        } else {
                            mediaPlayer.stop()
                            isPlaying = false
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }) {
                    Text(if (isPlaying) "Stop Audio" else "Play Episode")
                }
            }
        }
    }
}