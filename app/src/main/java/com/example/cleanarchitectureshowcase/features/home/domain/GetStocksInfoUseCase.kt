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

        val stocks = async {
            repository.getStocksList()
                .filter { it.exchangeShortName == DEFAULT_EXCHANGE }
                .shuffled()
        }.await()

        val limitedStocks =
            if (stocks.size > REQUEST_LIMIT) stocks.subList(0, REQUEST_LIMIT)
            else stocks

        val stockSymbols =
            limitedStocks
                .map { it.toDomain() }
                .reduceOrNull { acc, value -> "$acc,$value" }

        if (stockSymbols.isNullOrEmpty()) {
            return@withContext emptyList<SnippetData>()
        }

        val stockInfoList = async{
            repository.getStockInfo(stockSymbols)
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
        const val REQUEST_LIMIT = 10
    }
}
