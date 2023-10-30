package com.example.cleanarchitectureshowcase.features.home.domain

interface BusinessLogicHelper {
    suspend fun doWork(params: DataDomain): DataDomain

}