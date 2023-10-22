package com.example.cleanarchitectureshowcase.features.home.domain

import com.example.cleanarchitectureshowcase.core.domain.CoroutinesUseCase
import com.example.cleanarchitectureshowcase.features.home.presentation.SnippetData
import javax.inject.Inject

class GetMockDataUsecase @Inject constructor(
    private val repository: DataRepository,
    private val businessLogicHelper: BusinessLogicHelper
): CoroutinesUseCase<String, SnippetData> {

    override suspend fun invoke(params: String): SnippetData {
        val serverData = repository.getData("APPL")
        val result = businessLogicHelper.doWork(serverData.toDomain())
        return result.toUI()
    }
}