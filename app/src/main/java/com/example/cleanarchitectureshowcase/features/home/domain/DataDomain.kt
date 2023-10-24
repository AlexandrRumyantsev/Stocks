package com.example.cleanarchitectureshowcase.features.home.domain

import com.example.cleanarchitectureshowcase.features.home.presentation.SnippetData

data class DataDomain(
    val symbol: String,
    val companyName: String,
    val price: Double,
    val changes: Double,
    val image: String,
) {
    fun toUI() : SnippetData{
        return SnippetData(symbol, companyName, price, changes, changes/(price-changes), image)
    }
}
