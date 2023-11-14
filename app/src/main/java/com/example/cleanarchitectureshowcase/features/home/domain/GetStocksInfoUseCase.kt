package com.example.cleanarchitectureshowcase.features.home.domain

import com.example.cleanarchitectureshowcase.core.domain.CoroutinesUseCase
import com.example.cleanarchitectureshowcase.features.home.data.StockInfoModel
import com.example.cleanarchitectureshowcase.features.home.presentation.SnippetData
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.util.Collections


class GetStocksInfoUseCase @Inject constructor(
    private val repository: DataRepository,
    private val calculatingHelper: CalculatingHelper
): CoroutinesUseCase<String, List<SnippetData>> {

    override suspend fun invoke(params: String): List<SnippetData> = withContext(Dispatchers.IO) {

        val stocks = async {
            repository.getStocksList()
                .filter { it.exchangeShortName == DEFAULT_EXCHANGE }
                .shuffled()
        }.await()

        val limitedStocks =
            if (stocks.size > REQUEST_LIMIT) stocks.subList(0, REQUEST_LIMIT)
            else stocks

        val stockInfoList =
            limitedStocks
                .map{
                    async {
                        repository.getStockInfo(it.toDomain())
                    }
        }.awaitAll()

        val result = stockInfoList.map{
            val dataDomainItem = it[0].toDomain()
            dataDomainItem.toUI(percentage = calculatingHelper.calculatePercent(dataDomainItem))
        }
        return@withContext result
    }
    companion object {
        const val DEFAULT_EXCHANGE = "NASDAQ"
        const val REQUEST_LIMIT = 10
    }
}
