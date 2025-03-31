package com.example.itunes.ui.search

import com.example.itunes.utils.DataStatus
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.itunes.ui.player.AudioPlayerScreen
import com.example.itunes.ui.player.PlayerViewModel
import com.example.itunes.ui.theme.ItunesTheme
import com.example.itunes.utils.components.FullScreenLoader
import com.example.itunes.utils.components.FullScreenLoadingManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"

    private val viewModel: SearchViewModel by inject<SearchViewModel>()
    private val playerViewModel: PlayerViewModel by inject<PlayerViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ItunesTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ), title = {
                            Text("iTunes Search")
                        })
                }, bottomBar = {
                    AudioPlayerScreen(playerViewModel)
                    Spacer(modifier = Modifier.height(60.dp))
                }) { innerPadding ->
                    Log.e(TAG, "innerPadding: $innerPadding")
                    FullScreenLoader()
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SearchBar(
                            modifier = Modifier.padding(top = 88.dp), viewModel = viewModel
                        )
                        RecyclerView(
                            viewModel = viewModel, onItemClick = { index, item ->
                                Log.e(TAG, "Item clicked ${item.trackName} index ${index}")
                                playerViewModel.prepare(
                                    currentTrack = item, currentTrackIndex = index
                                )
                            })

                    }
                }
            }
        }
        initObserver()
    }

    private fun initObserver() {
        viewModel.musicTracks.observe(this) { response ->
            if (response.status == DataStatus.Status.LOADING) {
                FullScreenLoadingManager.showLoader()
            } else {
                FullScreenLoadingManager.hideLoader()
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, viewModel: SearchViewModel) {
    val text = remember { mutableStateOf("") }
    OutlinedTextField(
        value = text.value,
        onValueChange = { value ->
            text.value = value
            viewModel.setSearchText(value)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search icon"
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { viewModel.search(text.value) }),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            cursorColor = Color.Blue
        )
    )
}

