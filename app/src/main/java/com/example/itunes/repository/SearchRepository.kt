package com.example.itunes.repository

import com.example.itunes.utils.DataStatus
import android.util.Log
import com.example.itunes.data.remote.apis.search.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepository(private val apiService: ApiService) {

    val tag = SearchRepository::class.simpleName

    suspend fun search(searchTerm: String) = flow {
        emit(DataStatus.loading())
        val result = apiService.search(searchTerm)

        when (result.code()) {
            200 -> emit(DataStatus.success(result.body()?.results))
            400 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }.catch {
        it.message?.let { it1 -> Log.e(tag, it1) }
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}
