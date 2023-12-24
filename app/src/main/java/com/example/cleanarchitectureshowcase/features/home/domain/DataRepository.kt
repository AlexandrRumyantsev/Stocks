package com.example.cleanarchitectureshowcase.features.home.domain

import com.example.cleanarchitectureshowcase.features.home.data.SearchStockModel
import com.example.cleanarchitectureshowcase.features.home.data.StockInfoModel
import com.example.cleanarchitectureshowcase.features.home.data.StockModel

interface DataRepository {
    suspend fun getStockInfo(symbol: String): List<StockInfoModel>
    suspend fun getStocksList(): List<StockModel>
    suspend fun getStocksBySearch(
        symbol: String,
        limit: Int?,
        exchange: String ): List<SearchStockModel>
}
