package com.example.ui.audio

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class AudioPlaybackState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val title: String = "",
    val subtitle: String = "",
    val url: String = "",
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val isCompleted: Boolean = false
)

class AudioPlayerManager(private val context: Context) {
    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    private val _playbackState = MutableStateFlow(AudioPlaybackState())
    val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    init {
        initPlayer()
    }

    private fun initPlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _playbackState.value = _playbackState.value.copy(isPlaying = isPlaying)
                        if (isPlaying) {
                            startProgressTracking()
                        } else {
                            progressJob?.cancel()
                        }
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_BUFFERING -> {
                                _playbackState.value = _playbackState.value.copy(isBuffering = true)
                            }
                            Player.STATE_READY -> {
                                _playbackState.value = _playbackState.value.copy(
                                    isBuffering = false,
                                    durationMs = duration.coerceAtLeast(0L)
                                )
                            }
                            Player.STATE_ENDED -> {
                                _playbackState.value = _playbackState.value.copy(
                                    isPlaying = false,
                                    isCompleted = true,
                                    currentPositionMs = duration
                                )
                                progressJob?.cancel()
                            }
                            Player.STATE_IDLE -> {
                                _playbackState.value = _playbackState.value.copy(isBuffering = false)
                            }
                        }
                    }
                })
            }
        }
    }

    fun play(url: String, title: String, subtitle: String) {
        initPlayer()
        val player = exoPlayer ?: return

        if (_playbackState.value.url == url && player.playbackState != Player.STATE_IDLE) {
            if (!player.isPlaying) {
                player.play()
            }
            return
        }

        _playbackState.value = AudioPlaybackState(
            isPlaying = true,
            isBuffering = true,
            title = title,
            subtitle = subtitle,
            url = url,
            playbackSpeed = _playbackState.value.playbackSpeed
        )

        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun resume() {
        exoPlayer?.play()
    }

    fun togglePlayPause() {
        val player = exoPlayer ?: return
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        _playbackState.value = _playbackState.value.copy(currentPositionMs = positionMs)
    }

    fun setSpeed(speed: Float) {
        exoPlayer?.playbackParameters = PlaybackParameters(speed)
        _playbackState.value = _playbackState.value.copy(playbackSpeed = speed)
    }

    fun stop() {
        progressJob?.cancel()
        exoPlayer?.stop()
        _playbackState.value = AudioPlaybackState()
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive) {
                exoPlayer?.let { p ->
                    _playbackState.value = _playbackState.value.copy(
                        currentPositionMs = p.currentPosition.coerceAtLeast(0L),
                        durationMs = p.duration.coerceAtLeast(0L)
                    )
                }
                delay(500)
            }
        }
    }

    fun release() {
        progressJob?.cancel()
        exoPlayer?.release()
        exoPlayer = null
    }
}
