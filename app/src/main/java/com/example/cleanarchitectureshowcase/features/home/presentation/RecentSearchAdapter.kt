package com.example.cleanarchitectureshowcase.features.home.presentation

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanarchitectureshowcase.R
import com.example.cleanarchitectureshowcase.databinding.SearchItemBinding

class RecentSearchAdapter(private val listener: ItemClickListener?)
    : RecyclerView.Adapter<RecentSearchAdapter.SearchHolder>() {
    constructor() : this(null)
    private var searchList = arrayListOf<String>()

    class SearchHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = SearchItemBinding.bind(view)
        fun bind(text: String, listener: ItemClickListener?) {
            binding.apply {
                tvSearchItem.text = text
            }
            itemView.setOnClickListener{
                listener?.onItemClick(text)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return SearchHolder(view)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.bind(searchList[position], listener)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }
    fun submitList(list: ArrayList<String>){
        searchList = list
        notifyDataSetChanged()
    }
    fun addToList(text: String){
        searchList.add(text)
        notifyDataSetChanged()
    }
    fun clone() :RecentSearchAdapter{
        val clone = RecentSearchAdapter(listener)
        clone.searchList = ArrayList(this.searchList)
        return clone
    }
    interface ItemClickListener{
        fun onItemClick(text: String)
    }
}
