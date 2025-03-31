package com.example.itunes.ui.search

import com.example.itunes.utils.DataStatus
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itunes.data.remote.model.MusicTrack
import com.example.itunes.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _musicTracks = MutableLiveData<DataStatus<List<MusicTrack>>>()
    val musicTracks: LiveData<DataStatus<List<MusicTrack>>>
        get() = _musicTracks

    private val _textSearch = MutableStateFlow("")
    val textSearch: StateFlow<String> = _textSearch.asStateFlow()

    init {
        viewModelScope.launch {
            textSearch.debounce(1000).collect { it ->
                search(it);
            }
        }
    }

    fun setSearchText(it: String) {
        _textSearch.value = it

    }

    fun search(term: String) = viewModelScope.launch {
        if (term.length > 3) {
            searchRepository.search(term).collect {
                _musicTracks.value = it
            }
        }
    }
}
