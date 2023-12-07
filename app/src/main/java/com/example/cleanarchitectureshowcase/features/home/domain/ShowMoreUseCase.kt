package com.example.cleanarchitectureshowcase.features.home.domain

import com.example.cleanarchitectureshowcase.core.domain.CoroutinesUseCase
import com.example.cleanarchitectureshowcase.features.home.presentation.SnippetData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShowMoreUseCase @Inject constructor(
    private val repository: DataRepository,
    private val calculatingHelper: CalculatingHelper
) : CoroutinesUseCase<String, List<SnippetData>>{
    override suspend fun invoke(query: String): List<SnippetData> = withContext(Dispatchers.IO) {
        val stocks = async {
            repository.getStocksBySearch(query, DEFAULT_LIMIT, DEFAULT_EXCHANGE)
        }.await()

        val stockInfoList = stocks
            .map{
                async {
                    repository.getStockInfo(it.toDomain())
                }
            }.awaitAll()

        val result = stockInfoList.map {
            val dataDomainItem = it[0].toDomain()
            dataDomainItem.toUI(percentage = calculatingHelper.calculatePercent(dataDomainItem))
        }
        return@withContext result
    }
    companion object{
        const val DEFAULT_EXCHANGE = "NASDAQ"
        const val DEFAULT_LIMIT = 30
    }
}
