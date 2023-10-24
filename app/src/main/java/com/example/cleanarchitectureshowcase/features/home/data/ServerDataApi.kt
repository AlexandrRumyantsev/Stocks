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
    companion object{
        const val API_KEY = "LdCysPVv86Ky2fFecn5iBYUBCucrvfz8"
        const val BASE_URL = "https://financialmodelingprep.com/api/v3/"
    }
}