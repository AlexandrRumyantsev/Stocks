package com.example.cleanarchitectureshowcase.features.home.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerDataApi {
    @GET("available-traded/list")
    suspend fun getStocksList(
        @Query("apikey") apikey: String = API_KEY
    ): List<StockModel>
    @GET("profile/{company}")
    suspend fun getStockInfo(
        @Path("company") company: String,
        @Query("apikey") apikey: String = API_KEY
    ): List<StockInfoModel>
    @GET("search")
    suspend fun getStocksBySearchWithLimit(
        @Query("query") query: String,
        @Query("limit") limit: String,
        @Query("exchange") exchange: String,
        @Query("apikey") apikey: String = API_KEY
    ): List<SearchStockModel>
    @GET("search")
    suspend fun getStocksBySearch(
        @Query("query") query: String,
        @Query("exchange") exchange: String,
        @Query("apikey") apikey: String = API_KEY
    ) : List<SearchStockModel>
    companion object{
        const val API_KEY = "VAi7W4fxJNZQTN45n2FVGtB5iaNFLNtj"
        const val BASE_URL = "https://financialmodelingprep.com/api/v3/"
    }
}
