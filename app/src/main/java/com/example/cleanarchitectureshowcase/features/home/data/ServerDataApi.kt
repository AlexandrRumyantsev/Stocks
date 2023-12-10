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
    suspend fun getStocksBySearch(
        @Query("query") query: String,
        @Query("limit") limit: Int,
        @Query("exchange") exchange: String,
        @Query("apikey") apikey: String = API_KEY
    ): List<SearchStockModel>
    companion object{
        const val API_KEY = "wzLV0lyEjzEn9nW0toumKpINWChzRokR"
        const val BASE_URL = "https://financialmodelingprep.com/api/v3/"
    }
}
