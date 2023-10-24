package com.example.cleanarchitectureshowcase.features.home.domain

import com.example.cleanarchitectureshowcase.core.domain.CoroutinesUseCase
import com.example.cleanarchitectureshowcase.features.home.data.StockInfoModel
import com.example.cleanarchitectureshowcase.features.home.presentation.SnippetData
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class GetStocksInfoUseCase @Inject constructor(
    private val repository: DataRepository,
    private val businessLogicHelper: BusinessLogicHelper
): CoroutinesUseCase<String, List<SnippetData>> {

    override suspend fun invoke(params: String): List<SnippetData> = withContext(Dispatchers.IO){
        var domainDataItem: DataDomain

        val firstTenStocks = async {
            repository.getStocksList().filter { it.exchangeShortName == DEFAULT_EXCHANGE }
                .shuffled()
        }.await().subList(0,10)
        val firstTenStocksAsString =
            firstTenStocks.map { it.symbol }.reduceOrNull { acc, value -> "$acc,$value" }
        if (firstTenStocksAsString.isNullOrEmpty()) {
            return@withContext emptyList<SnippetData>()
        }

        val stockInfoList = async{
            repository.getStockInfo(firstTenStocksAsString)
        }.await()

        val result = mutableListOf<SnippetData>()
        for (item in stockInfoList){
            domainDataItem = item.toDomain()
            businessLogicHelper.doWork(domainDataItem)
            result.add(domainDataItem.toUI())
        }
        result
    }
    companion object {
        const val DEFAULT_EXCHANGE = "NASDAQ"
    }
}
