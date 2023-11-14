package com.example.cleanarchitectureshowcase.features.home.domain

import com.example.cleanarchitectureshowcase.core.domain.CoroutinesUseCase
import com.example.cleanarchitectureshowcase.features.home.presentation.SnippetData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetStocksBySearchUseCase @Inject constructor(
    private val repository: DataRepository,
    private val calculatingHelper: CalculatingHelper
) : CoroutinesUseCase<String, List<SnippetData>>{
    override suspend fun invoke(query: String): List<SnippetData> = withContext(Dispatchers.IO){

        var domainDataItem: DataDomain
        val result = mutableListOf<SnippetData>()

        val stocks = async {
            repository.getStocksBySearch(query, DEFAULT_LIMIT, DEFAULT_EXCHANGE)
        }.await()

        val stockInfoList = stocks
            .map{
                async {
                    repository.getStockInfo(it.toDomain())
            }
        }.awaitAll()


        for (item in stockInfoList){
            domainDataItem = item[0].toDomain()
            val percentage = calculatingHelper.calculatePercent(domainDataItem)
            result.add(domainDataItem.toUI(percentage))
        }
        return@withContext result
    }
    companion object{
        const val DEFAULT_EXCHANGE = "NASDAQ"
        const val DEFAULT_LIMIT = 5
    }
}

