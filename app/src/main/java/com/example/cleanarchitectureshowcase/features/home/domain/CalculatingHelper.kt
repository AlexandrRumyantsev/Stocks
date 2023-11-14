package com.example.cleanarchitectureshowcase.features.home.domain

interface CalculatingHelper {
    suspend fun calculatePercent(params: DataDomain): Double

}
