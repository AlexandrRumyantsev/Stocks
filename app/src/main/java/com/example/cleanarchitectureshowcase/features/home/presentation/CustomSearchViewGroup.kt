package com.example.cleanarchitectureshowcase.features.home.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cleanarchitectureshowcase.R
import com.example.cleanarchitectureshowcase.databinding.SearchHintBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager

class CustomSearchViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    desStyleRes: Int = 0
) : ConstraintLayout(context,attrs, defStyleAttr, desStyleRes) {

    private val binding: SearchHintBinding
    private var layoutManager = FlexboxLayoutManager(context)
    private var adapter = RecentSearchAdapter()

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.search_hint, this, true)
        binding = SearchHintBinding.bind(this)
        initAttrs(attrs,defStyleAttr,desStyleRes)
        initRcView()
    }

    private fun initRcView() {
        binding.apply {
            rcView.layoutManager = layoutManager
            rcView.adapter = adapter
        }
    }

    private fun initAttrs(
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        desStyleRes: Int = 0
    ){
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomSearchViewGroup, defStyleAttr, desStyleRes)
        val title = typedArray.getString(R.styleable.CustomSearchViewGroup_titleText)
        binding.tvTitle.text = title
        typedArray.recycle()
    }

    fun setAdapter(adapter: RecentSearchAdapter){
        this.adapter = adapter.clone()
        binding.rcView.adapter = this.adapter
    }

    fun submitList(list: List<String>){
        adapter.submitList(ArrayList(list))
    }

    fun addToList(text: String){
        adapter.addToList(text)
    }
}
