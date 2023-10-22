package com.example.cleanarchitectureshowcase.features.home.domain

import com.example.cleanarchitectureshowcase.features.home.presentation.SnippetData

data class DataDomain(
    val symbol: String,
    val companyName: String,
    val price: Float,
    val changes: Float,
    val image: String,
) {
    fun toUI() : SnippetData{
        return SnippetData(symbol, companyName, "SHARE_COST", "DIFFERENCE", image)
    }
}
