package com.example.itunes.ui.search

import com.example.itunes.utils.DataStatus
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.itunes.data.remote.model.MusicTrack

@Composable
fun RecyclerView(viewModel: SearchViewModel, onItemClick: (Int, MusicTrack) -> Unit) {

    val musicTracks = viewModel.musicTracks.observeAsState(
        initial = DataStatus(
            status = DataStatus.Status.SUCCESS, data = emptyList(), message = ""
        )
    )

    val tracks = musicTracks.value.data

    LazyColumn {
        tracks?.let {
            itemsIndexed(it) { index, item ->
                MusicTrackItem(index, item, onItemClick = onItemClick)
            }
        }
        item {
            Spacer(
                modifier = Modifier.height(75.dp)
            )

        }
    }
}


@Composable
fun MusicTrackItem(index: Int, musicTrack: MusicTrack, onItemClick: (Int, MusicTrack) -> Unit) {
    Card(modifier = Modifier
        .clickable { onItemClick(index, musicTrack) }
        .fillMaxWidth()
        .padding(8.dp), colors = CardDefaults.cardColors(
        containerColor = Color.White, contentColor = Color.Black
    ), elevation = CardDefaults.cardElevation(
        defaultElevation = 8.dp
    ), shape = RoundedCornerShape(8.dp)) {
        Row(
            modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            CustomArtImage(musicTrack)
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = musicTrack.trackName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = musicTrack.artistName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                musicTrack.collectionName?.let {
                    Text(
                        text = it, style = MaterialTheme.typography.titleMedium, color = Color.Gray
                    )
                }
            }
        }
    }
}

fun getMatrix() = ColorMatrix().apply {
    setToSaturation(0f)
}

@Composable
fun CustomArtImage(musicTrack: MusicTrack) {
    Box(contentAlignment = Alignment.Center) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(musicTrack.artworkUrl100)
                .crossfade(true).build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(10)),
            colorFilter = ColorFilter.colorMatrix(getMatrix())
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .fillMaxSize()
                .background(color = Color.White.copy(alpha = 0.7f))
        ) {
            IconButton(
                onClick = { /* action */ }, modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    tint = Color.Black,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}