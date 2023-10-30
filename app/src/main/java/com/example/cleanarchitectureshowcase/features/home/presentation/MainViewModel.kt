package com.example.cleanarchitectureshowcase.features.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureshowcase.features.home.domain.GetStocksInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getStocksInfoUseCase: GetStocksInfoUseCase
): ViewModel() {
    val state = MutableStateFlow<List<SnippetData>?>(null)
    fun getStocksData(){
        viewModelScope.launch {
            state.value = getStocksInfoUseCase.invoke("")
        }
    }
}