package com.example.cleanarchitectureshowcase.features.home.data

import com.example.cleanarchitectureshowcase.features.home.domain.DataDomain
import com.google.gson.annotations.SerializedName

data class StockInfoModel(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("beta")
    val beta: Double,
    @SerializedName("volAvg")
    val volAvg: Int,
    @SerializedName("mktCap")
    val mktCap: Long,
    @SerializedName("lastDiv")
    val lastDiv: Double,
    @SerializedName("range")
    val range: String,
    @SerializedName("changes")
    val changes: Double,
    @SerializedName("companyName")
    val companyName: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("cik")
    val cik: String,
    @SerializedName("isin")
    val isin: String,
    @SerializedName("cusip")
    val cusip: String,
    @SerializedName("exchange")
    val exchange: String,
    @SerializedName("exchangeShortName")
    val exchangeShortName: String,
    @SerializedName("industry")
    val industry: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("ceo")
    val ceo: String,
    @SerializedName("sector")
    val sector: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("fullTimeEmployees")
    val fullTimeEmployees: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("zip")
    val zip: String,
    @SerializedName("dcfDiff")
    val dcfDiff: Double,
    @SerializedName("dcf")
    val dcf: Double,
    @SerializedName("image")
    val image: String,
    @SerializedName("ipoDate")
    val ipoDate: String,
    @SerializedName("defaultImage")
    val defaultImage: Boolean,
    @SerializedName("isEtf")
    val isEtf: Boolean,
    @SerializedName("isActivelyTrading")
    val isActivelyTrading: Boolean,
    @SerializedName("isAdr")
    val isAdr: Boolean,
    @SerializedName("isFund")
    val isFund: Boolean
) {
    fun toDomain() = DataDomain(
        symbol, companyName, price, changes, image
    )
}
