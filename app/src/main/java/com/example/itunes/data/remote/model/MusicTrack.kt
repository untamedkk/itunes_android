package com.example.itunes.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MusicTrack(
    @Json(name = "artistName")
    val artistName: String,

    @Json(name = "collectionName")
    val collectionName: String?,

    @Json(name = "trackName")
    val trackName: String,

    @Json(name = "previewUrl")
    val previewUrl: String,

    @Json(name = "artworkUrl100")
    val artworkUrl100: String,

    @Json(name = "trackTimeMillis")
    val trackTimeMillis: Int,

    )