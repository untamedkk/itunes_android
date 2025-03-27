package com.example.itunes.data.remote.response

import com.example.itunes.data.remote.model.MusicTrack
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MusicSearchResult(
    @Json(name = "resultCount")
    val resultCount: Int,

    @Json(name = "results")
    val results: List<MusicTrack>,
)