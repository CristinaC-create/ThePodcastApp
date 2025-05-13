package com.cristinacabral.thepodcastapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            NavHost(navController = navController, startDestination = "podcasts") {
                composable("podcasts") {
                    PodcastListScreen(navController)
                }
            }
        }
    }
}

data class Podcast(
    val title: String,
    val description: String,
    val imageUrl: String,
    val websiteUrl: String,
    val audioUrl: String
)

val samplePodcasts = listOf(
    Podcast(
        "The Daily",
        "News and insights from The New York Times.",
        "https://static01.nyt.com/images/2019/06/18/podcasts/the-daily-album-art/the-daily-album-art-square320.jpg",
        "https://www.nytimes.com/column/the-daily",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
    ),
    Podcast(
        "99% Invisible",
        "Design and architecture stories you didn’t know you needed.",
        "https://99percentinvisible.org/app/uploads/2021/02/99invisible_logo-320x320.png",
        "https://99percentinvisible.org/",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
    ),
    Podcast(
        "SmartLess",
        "Funny, inspiring, and insightful conversations with celebrities.",
        "https://m.media-amazon.com/images/I/71m7d3hn5iL._SL500_.jpg",
        "https://www.smartless.com/",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
    ),
    Podcast(
        "Planet Money",
        "The economy explained in a fun, engaging way.",
        "https://media.npr.org/assets/img/2021/07/06/planet-money_sq-187379f9759d0030f7c09c6ce1c5a92edaa9d31d.jpg",
        "https://www.npr.org/sections/money/",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"
    ),
    Podcast(
        "Science Vs",
        "Science Vs myths, fads and bad advice.",
        "https://images.megaphone.fm/sVm5cdOjEpYWSNkFdqbs8vqw7iG1Q0RA0DWU1WXvbh4/plain/s3://megaphone-prod/podcasts/776fb4e8-6db1-11e8-b14a-ff3e84fb8c35/image/uploads_2F1572487179582-6v1k6ge7evm-b9e92b7264e64e9a6d35c9db278f90d5_2Fsciencevs_2020_final.jpg",
        "https://gimletmedia.com/shows/science-vs",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"
    )
)

@Composable
fun PodcastListScreen(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "\uD83C\uDFA7 Featured Podcasts",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0288D1)
        )

        samplePodcasts.forEach { podcast ->
            PodcastCard(podcast = podcast) {
                val intent = Intent(Intent.ACTION_VIEW, podcast.websiteUrl.toUri())
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun PodcastCard(podcast: Podcast, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(podcast.imageUrl),
                contentDescription = podcast.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            Text(podcast.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(podcast.description, fontSize = 16.sp, color = Color.DarkGray)
        }
    }
}