package com.example.cleanarchitectureshowcase.features.home.domain

import com.example.cleanarchitectureshowcase.features.home.data.StockDTO

interface DataRepository {
    suspend fun getData(symbol: String): StockDTO
}