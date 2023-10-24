package com.example.cleanarchitectureshowcase.features.home.data

import com.google.gson.annotations.SerializedName

data class StockModel(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("exchange")
    val exchange: String,
    @SerializedName("exchangeShortName")
    val exchangeShortName: String,
    @SerializedName("price")
    val price: Float
) {
    fun toDomain() : String = symbol
}
