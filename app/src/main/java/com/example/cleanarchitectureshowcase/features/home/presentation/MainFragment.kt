package com.example.cleanarchitectureshowcase.features.home.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleanarchitectureshowcase.R
import com.example.cleanarchitectureshowcase.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var stocksAdapter: StocksAdapter
    private lateinit var searchAdapter: RecentSearchAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewBinding()
        setupListeners()
        setupSubscriptions()
    }

    private fun initViewPager() = with(binding) {
        val adapter = VpAdapter(activity as AppCompatActivity, fList)
        vp.adapter = adapter
        TabLayoutMediator(tabLayout, vp){
            tab, pos -> tab.text = tList[pos]
        }.attach()
    }

    private fun initRvSearching() = with(binding){
        val listener = object: StocksAdapter.StockItemClickListener{
            override fun onItemCLick(item: SnippetData) {
                viewModel.addToHistory(item.abbreviation)
                saveSearchHistory()
            }
        }
        rvSearching.layoutManager = LinearLayoutManager(activity)
        stocksAdapter = StocksAdapter(listener)
        rvSearching.adapter = stocksAdapter
    }

    private fun initRvRecentSearch() = with(binding){
        val listener = object : RecentSearchAdapter.ItemClickListener {
            override fun onItemClick(text: String) {
                searchView.setQuery(text, true)
            }
        }
        searchAdapter = RecentSearchAdapter(listener)
        cvSearchPopular.setAdapter(searchAdapter)
        cvSearchRecent.setAdapter(searchAdapter)
        fillPopularRequests()
        loadSearchHistory()
    }

    private fun setupSubscriptions() = with(binding){

        lifecycleScope.launch {
            viewModel.searchState.collect {
                it?.let{
                    stocksAdapter.submitList(it)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.isSearchingState.collect{
                it?.let {
                    when(it) {
                        MainViewModel.SEARCH_STOPPED -> showAllStocksInterface()
                        MainViewModel.SEARCH_READY -> showStartSearchingInterface()
                        MainViewModel.SEARCH_STARTED -> showSearchingInterface()
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.searchHistoryState.collect{
                it?.let {
                    cvSearchRecent.submitList(viewModel.getSearchHistory())
                }
            }
        }
    }

    private fun fillPopularRequests() = with(binding){
        val popularRequests = resources.getStringArray(R.array.popular_requests).asList()
        cvSearchPopular.submitList(popularRequests)
    }

    private fun setupViewBinding() = with(binding){
        initViewPager()
        initRvSearching()
        initRvRecentSearch()
    }

    private fun setupListeners() = with(binding){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchStocksByQuery(newText)
                return true
            }
        })
        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            viewModel.updateSearchingStatus(hasFocus)
        }
        tvShowMore.setOnClickListener {
            viewModel.showMoreStocks(binding.searchView.query.toString())
        }
        tvClearSearch.setOnClickListener {
            viewModel.clearSearchHistory()
            saveSearchHistory()
        }
        root.setOnClickListener {
            binding.searchView.clearFocus()
        }
    }

    private fun showStartSearchingInterface() = with(binding) {
        tabLayout.visibility = View.GONE
        vp.visibility = View.GONE
        divider.visibility = View.GONE
        tvStocks.visibility = View.GONE
        tvShowMore.visibility = View.GONE
        rvSearching.visibility = View.GONE
        cvSearchPopular.visibility = View.VISIBLE
        cvSearchRecent.visibility = View.VISIBLE
        tvClearSearch.visibility = View.VISIBLE
    }
    private fun showSearchingInterface() = with(binding) {
        tabLayout.visibility = View.GONE
        vp.visibility = View.GONE
        divider.visibility = View.VISIBLE
        tvStocks.visibility = View.VISIBLE
        tvShowMore.visibility = View.VISIBLE
        rvSearching.visibility = View.VISIBLE
        cvSearchPopular.visibility = View.GONE
        cvSearchRecent.visibility = View.GONE
        tvClearSearch.visibility = View.GONE
    }
    private fun showAllStocksInterface() = with(binding) {
        tabLayout.visibility = View.VISIBLE
        vp.visibility = View.VISIBLE
        divider.visibility = View.GONE
        tvStocks.visibility = View.GONE
        tvShowMore.visibility = View.GONE
        rvSearching.visibility = View.GONE
        cvSearchPopular.visibility = View.GONE
        cvSearchRecent.visibility = View.GONE
        tvClearSearch.visibility = View.GONE
    }
    private fun saveSearchHistory() {
        val sPref = activity?.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
        val editor = sPref?.edit()
        val gson = Gson()
        val json = gson.toJson(viewModel.getSearchHistory())
        editor?.putString(getString(R.string.history), json)
        editor?.apply()
    }
    private fun loadSearchHistory() {
        val sPref = activity?.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sPref?.getString(getString(R.string.history), null)
        val type = object : TypeToken<ArrayList<String>>(){}.type
        json?.let {
            val history: ArrayList<String> = gson.fromJson(it, type)
            binding.cvSearchRecent.submitList(history)
            history.map { item ->
                viewModel.addToHistory(item)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveSearchHistory()
    }
    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
        private val fList = listOf(
            StocksFragment.newInstance(),
            FavoriteFragment.newInstance()
        )
        private val tList = listOf(
            "Stocks",
            "Favorite"
        )
    }
}
