package com.example.cleanarchitectureshowcase.features.home.domain

class CalculatingHelperImpl : CalculatingHelper {

    override suspend fun calculatePercent(params: DataDomain): Double {
        return params.changes / (params.price - params.changes)
    }
}
