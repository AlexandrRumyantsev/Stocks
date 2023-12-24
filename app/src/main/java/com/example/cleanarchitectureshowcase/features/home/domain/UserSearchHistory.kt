package com.example.cleanarchitectureshowcase.features.home.domain

import dagger.Provides
import javax.inject.Inject

class UserSearchHistory {

    private val searchHistory = arrayListOf<String>()
    fun addToHistory(query: String): Boolean{
        if (searchHistory.contains(query) || query.isEmpty())
            return false
        if (searchHistory.size == MAX_HISTORY_SIZE){
            searchHistory.removeFirst()
        }
        searchHistory.add(query)
        return true
    }
    fun clearSearchHistory(){
        searchHistory.clear()
    }
    fun getSearchHistory() : MutableList<String>{
        return searchHistory
    }
    companion object{
        const val MAX_HISTORY_SIZE = 6
    }
}

