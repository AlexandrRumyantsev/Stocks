package com.example.cleanarchitectureshowcase.features.home.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanarchitectureshowcase.databinding.FragmentStocksBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class StocksFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentStocksBinding
    private lateinit var stocksAdapter: StocksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStocksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvStocks()
        setupSubscriptions()
        viewModel.getStocksData()
    }

    private fun initRvStocks() = with(binding){
        showProgressBar(binding.progressBar.progressBar)
        rvStocks.layoutManager = LinearLayoutManager(activity)
        stocksAdapter = StocksAdapter()
        rvStocks.adapter = stocksAdapter
        rvStocks.addOnScrollListener(
            object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if(!recyclerView.canScrollVertically(1)) {
                        showProgressBar(binding.progressBar.progressBar)
                        viewModel.getStocksData()
                    }
                }
            }
        )
    }

    private fun setupSubscriptions(){
        lifecycleScope.launch {
            viewModel.stocksState.collect {
                it?.let{
                    stocksAdapter.submitList(it)
                    hideProgressBar(binding.progressBar.progressBar)
                }
            }
        }
    }

    private fun showProgressBar(progressBar: ProgressBar){
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(progressBar: ProgressBar){
        progressBar.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() = StocksFragment()
    }
}
