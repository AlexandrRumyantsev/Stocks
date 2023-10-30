package com.example.cleanarchitectureshowcase.features.home.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cleanarchitectureshowcase.R
import com.example.cleanarchitectureshowcase.databinding.SnippetBinding
import kotlin.math.abs

class StocksAdapter : ListAdapter<SnippetData, StocksAdapter.StockHolder>(Comparator()){
    class StockHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = SnippetBinding.bind(view)

        fun bind(stockItem: SnippetData, position: Int) = with(binding){
            val cardColor =
                if (position%2 == 0) R.color.card_bg else R.color.white
            val hasPriceIncreased: Boolean = stockItem.difference > 0
            tvCompanyAbbreviation.text = stockItem.abbreviation
            tvCompany.text = stockItem.company
            tvShareCost.text = "$".plus(stockItem.shareCost.toString())
            tvDifference.text =
                "$"
                .plus(abs(stockItem.difference))
            tvDifferencePercentage.text =
                "("
                .plus(String.format("%.4f", abs(stockItem.differencePercentage)))
                .plus("%)")
            if(hasPriceIncreased){
                tvDifferencePercentage.setTextAppearance(R.style.text_subtitle_green)
                tvDifference.setTextAppearance(R.style.text_subtitle_green)
                tvDifference.text = "+".plus(tvDifference.text)
            }
            else {
                tvDifferencePercentage.setTextAppearance(R.style.text_subtitle_red)
                tvDifference.setTextAppearance(R.style.text_subtitle_red)
                tvDifference.text = "-".plus(tvDifference.text)
            }
            Glide.with(itemView.context)
                .load(stockItem.companyPic)
                .error(R.drawable.diagram_bar_downtrend_svgrepo_com)
                .placeholder(R.color.white)
                .into(ivCompanyPic)
            root.setCardBackgroundColor(ContextCompat.getColor(itemView.context, cardColor))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.snippet, parent, false)
        return StockHolder(view)
    }
    override fun onBindViewHolder(holder: StockHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class Comparator: DiffUtil.ItemCallback<SnippetData>(){
        override fun areItemsTheSame(oldItem: SnippetData, newItem: SnippetData): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: SnippetData, newItem: SnippetData): Boolean {
            return oldItem == newItem
        }
    }
}
