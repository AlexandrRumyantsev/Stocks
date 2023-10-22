package com.example.cleanarchitectureshowcase.features.home.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerDataApi {
    @GET("financial-statement-symbol-lists")
    suspend fun getStocksList(
        @Query("apikey") apikey: String = API_KEY
    ): List<String>
    @GET("profile/{company}")
    suspend fun getStockDTO(
        @Path("company") company: String,
        @Query("apikey") apikey: String = API_KEY
    ): StockDTO
    companion object{
        const val API_KEY = "43ea0c65b8688f853928dee8bee20e8d"
        const val BASE_URL = "https://financialmodelingprep.com/api/v3/"
    }
}