package com.example.cleanarchitectureshowcase.features.home.data

import com.google.gson.annotations.SerializedName

data class SearchStockModel(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("stockExchange")
    val stockExchange: String,
    @SerializedName("exchangeStockName")
    val exchangeStockName: String
) {
    fun toDomain(): String = symbol
}
