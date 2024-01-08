package com.example.cleanarchitectureshowcase.features.home.domain

import com.example.cleanarchitectureshowcase.core.domain.CoroutinesUseCase
import com.example.cleanarchitectureshowcase.features.home.data.StockModel
import com.example.cleanarchitectureshowcase.features.home.presentation.SnippetData
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext


class GetStocksInfoUseCase @Inject constructor(
    private val repository: DataRepository,
    private val calculatingHelper: CalculatingHelper
): CoroutinesUseCase<List<SnippetData>?, List<SnippetData>> {

    private var stocks: List<StockModel>? = null
    private var currentPage = 1;
    override suspend fun invoke(params: List<SnippetData>?): List<SnippetData> = withContext(Dispatchers.IO) {
        if (stocks == null) {
            stocks = async {
                repository.getStocksList()
                    .filter { it.exchangeShortName == DEFAULT_EXCHANGE }
                    .shuffled()
            }.await()
        }
        val limitedStocks = stocks?.subList((currentPage-1)* REQUEST_LIMIT, currentPage* REQUEST_LIMIT)
        val stockInfoList =
            limitedStocks
                ?.map{
                    async {
                         repository.getStockInfo(it.toDomain())
                    }
                }?.awaitAll()
        val tmpResult = stockInfoList?.map{
            val dataDomainItem = it[0].toDomain()
            dataDomainItem.toUI(percentage = calculatingHelper.calculatePercent(dataDomainItem))
        }?.toMutableList()
        if (params == null)
            return@withContext tmpResult!!
        val result = mutableListOf<SnippetData>()
        if (tmpResult != null) {
            result.addAll(params)
            result.addAll(tmpResult)
        }
        currentPage++
        return@withContext result
    }
    companion object {
        const val DEFAULT_EXCHANGE = "NASDAQ"
        const val REQUEST_LIMIT = 3
    }
}
