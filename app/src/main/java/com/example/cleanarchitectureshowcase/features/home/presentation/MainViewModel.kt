package com.example.cleanarchitectureshowcase.features.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureshowcase.features.home.domain.GetStocksBySearchUseCase
import com.example.cleanarchitectureshowcase.features.home.domain.GetStocksInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getStocksInfoUseCase: GetStocksInfoUseCase,
    private val searchStocksUseCase: GetStocksBySearchUseCase
): ViewModel() {
    private var searchJob: Job? = null
    val stocksState = MutableStateFlow<List<SnippetData>?>(null)
    val searchState = MutableStateFlow<List<SnippetData>?>(null)
    fun getStocksData(){
        viewModelScope.launch {
            stocksState.value = getStocksInfoUseCase.invoke("")
        }
    }
    fun searchStocksByQuery(query: String){
        searchJob = viewModelScope.launch {
            delay(400)
            searchState.value = searchStocksUseCase.invoke(query)
        }
    }
    fun cancelJob(){
        searchJob?.cancel()
        searchJob = null
    }
}
