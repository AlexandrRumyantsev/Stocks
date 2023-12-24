package com.example.cleanarchitectureshowcase.features.home.data

import com.example.cleanarchitectureshowcase.features.home.domain.DataRepository

class DataRepositoryImpl(
    private val api: ServerDataApi
): DataRepository {

    override suspend fun getStockInfo(symbol: String): List<StockInfoModel> {
        return api.getStockInfo(symbol)
    }
    override suspend fun getStocksList(): List<StockModel> {
        return api.getStocksList()
    }

    override suspend fun getStocksBySearch(
        symbol: String,
        limit: Int?,
        exchange: String
    ): List<SearchStockModel> {
        val limitToString = limit?.toString() ?: ""
        return api.getStocksBySearchWithLimit(symbol,limitToString,exchange)
    }
}