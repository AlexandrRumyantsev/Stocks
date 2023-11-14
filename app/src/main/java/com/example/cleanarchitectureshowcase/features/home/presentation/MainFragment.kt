package com.example.cleanarchitectureshowcase.features.home.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleanarchitectureshowcase.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: StocksAdapter
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
        initViewPager()
        initRvSearching()
        showAllStocksInterface()
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() == true) {
                    adapter
                    showSearchingInterface()
                    lifecycleScope.launch {
                        viewModel.cancelJob()
                        viewModel.searchStocksByQuery(newText)
                        viewModel.searchState.collectLatest {
                            adapter.submitList(it)
                        }
                    }
                }
                else {
                    viewModel.cancelJob()
                    showAllStocksInterface()
                }
                return true
            }
        })
    }

    private fun initViewPager() = with(binding) {
        val adapter = VpAdapter(activity as AppCompatActivity, fList)
        vp.adapter = adapter
        TabLayoutMediator(tabLayout, vp){
            tab, pos -> tab.text = tList[pos]
        }.attach()
    }
    private fun initRvSearching() = with(binding){
        rvSearching.layoutManager = LinearLayoutManager(activity)
        adapter = StocksAdapter()
        rvSearching.adapter = adapter
    }
    private fun showSearchingInterface() = with(binding){
        tabLayout.visibility = View.GONE
        vp.visibility = View.GONE
        divider.visibility = View.VISIBLE
        tvStocks.visibility = View.VISIBLE
        tvShowMore.visibility = View.VISIBLE
        rvSearching.visibility = View.VISIBLE
    }
    private fun showAllStocksInterface() = with(binding){
        tabLayout.visibility = View.VISIBLE
        vp.visibility = View.VISIBLE
        divider.visibility = View.GONE
        tvStocks.visibility = View.GONE
        tvShowMore.visibility = View.GONE
        rvSearching.visibility = View.GONE
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
