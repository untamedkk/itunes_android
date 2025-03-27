package com.example.itunes.data.remote.apis.search

import com.example.itunes.data.remote.response.MusicSearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(Endpoints.SEARCH + "?entity=musicTrack")
    suspend fun search(@Query("term") term: String): Response<MusicSearchResult>
}