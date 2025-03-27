package com.example.itunes.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.itunes.data.remote.model.MusicTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val exoPlayer: ExoPlayer
) : ViewModel(), Player.Listener {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private lateinit var currentTrack: MusicTrack;

    init {
        exoPlayer.addListener(this)
    }


    fun prepare(currentTrack: MusicTrack, currentTrackIndex: Int) {
        this.currentTrack = currentTrack
        updateState { copy(currentTrack = currentTrack, currentTrackIndex = currentTrackIndex) }
        if (exoPlayer.isPlaying) {
            exoPlayer.stop()
        }
        exoPlayer.setMediaItem(MediaItem.fromUri(currentTrack.previewUrl))
        exoPlayer.prepare()

        togglePlayPause()
    }

    fun togglePlayPause() {
        viewModelScope.launch {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
            updateState {
                copy(
                    isPlaying = exoPlayer.isPlaying,
                    title = currentTrack?.trackName ?: ""
                )
            }
        }
    }

    fun nextTrack() {
        viewModelScope.launch {
            if (exoPlayer.hasNextMediaItem()) {
                exoPlayer.seekToNextMediaItem()
                updateState {
                    copy(
                        isPlaying = true,
                        currentTrackIndex = exoPlayer.currentMediaItemIndex,
                        title = currentTrack?.trackName ?: ""
                    )
                }
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            Player.STATE_ENDED, Player.STATE_IDLE -> updateState { copy(isPlaying = false) }
            Player.STATE_BUFFERING -> updateState { copy(isPlaying = false) }
            Player.STATE_READY -> updateState { copy(isPlaying = true) }
        }
    }

    private fun handleTrackEnded() {
        if (exoPlayer.hasNextMediaItem()) {
            nextTrack()
        } else {
            updateState { copy(isPlaying = false) }
        }
    }

    private fun updateState(update: PlayerState.() -> PlayerState) {
        _playerState.value = _playerState.value.update()
    }

    override fun onCleared() {
        exoPlayer.removeListener(this)
        exoPlayer.release()
        super.onCleared()
    }

    data class PlayerState(
        val isPlaying: Boolean = false,
        val currentTrackIndex: Int = -1,
        val currentTrack: MusicTrack? = null,
        val title: String = ""
    )

}