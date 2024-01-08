package com.example.cleanarchitectureshowcase.features.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureshowcase.features.home.domain.GetStocksBySearchUseCase
import com.example.cleanarchitectureshowcase.features.home.domain.GetStocksInfoUseCase
import com.example.cleanarchitectureshowcase.features.home.domain.ShowMoreUseCase
import com.example.cleanarchitectureshowcase.features.home.domain.UserSearchHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getStocksInfoUseCase: GetStocksInfoUseCase,
    private val searchStocksUseCase: GetStocksBySearchUseCase,
    private val showMoreUseCase: ShowMoreUseCase,
    private val searchHistory: UserSearchHistory
): ViewModel() {

    private var searchJob: Job? = null
    val isSearchingState = MutableStateFlow<Int?>(0)
    val stocksState = MutableStateFlow<List<SnippetData>?>(null)
    val searchState = MutableStateFlow<List<SnippetData>?>(null)
    val searchHistoryState = MutableStateFlow<String?>(null)

    fun getStocksData() {
        viewModelScope.launch {
            stocksState.value = getStocksInfoUseCase.invoke(stocksState.value)
        }
    }

    fun showMoreStocks(query: String?){
        viewModelScope.launch{
            query?.let{
                searchState.value = showMoreUseCase.invoke(it)
            }
        }
    }

    fun searchStocksByQuery(query: String?) {
        updateIsSearchingState(query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE)
            query?.let {
                searchState.value = searchStocksUseCase.invoke(it)
            }
        }
    }
    fun addToHistory(searchItem: String) {
        if(searchHistory.addToHistory(searchItem)) {
            updateSearchHistoryState(searchItem)
        }
    }
    fun clearSearchHistory() {
        searchHistory.clearSearchHistory()
        updateSearchHistoryState("")
    }
    fun updateSearchingStatus(hasFocus: Boolean){
        if(hasFocus && isSearchingState.value != SEARCH_STARTED) isSearchingState.value = SEARCH_READY
        if(!hasFocus && isSearchingState.value == SEARCH_READY) isSearchingState.value = SEARCH_STOPPED
    }

    fun getSearchHistory(): MutableList<String> {
        return searchHistory.getSearchHistory()
    }

    private fun updateSearchHistoryState(query: String) {
        searchHistoryState.value = query
    }

    private fun updateIsSearchingState(query: String?){
        query?.let {
            if(it.isNotEmpty()) isSearchingState.value = SEARCH_STARTED
            else isSearchingState.value = SEARCH_READY
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE: Long = 400
        const val SEARCH_STOPPED = 0
        const val SEARCH_READY = 1
        const val SEARCH_STARTED = 2
    }
}
