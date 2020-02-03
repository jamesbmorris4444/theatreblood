package com.fullsekurity.theatreblood.recyclerview

import android.view.View
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


abstract class RecyclerViewFilterAdapter<T, VM : RecyclerViewItemViewModel<T>> :  RecyclerView.Adapter<RecyclerViewFilterAdapter.ItemViewHolder<T, VM>>(), Filterable {

    private var recyclerView: RecyclerView? = null
    private var adapterFilter: AdapterFilter? = null

    val itemList: MutableList<T> = mutableListOf()
    private var itemListFiltered: MutableList<T> = mutableListOf()

    private val filter: AdapterFilter
        get() {
            if (adapterFilter == null) {
                adapterFilter = AdapterFilter()
            }
            return adapterFilter ?: AdapterFilter()
        }

    override fun onBindViewHolder(holder: ItemViewHolder<T, VM>, position: Int) {
        holder.setItem(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addAll(toAddList: List<T>) {
        clearAll()
        itemListFiltered.addAll(toAddList)
        if (isFiltering()) {
            adapterFilter?.filter()
        } else {
            itemList.addAll(toAddList)
            if (recyclerView != null) {
                recyclerView?.recycledViewPool?.clear()
            }
            notifyDataSetChanged()
        }
    }

    fun remove(position: Int) {
        if (itemCount > position) {
            itemList.removeAt(position)
            itemListFiltered.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun contains(item: T, position: Int): Boolean {
        return itemList[position] == item
    }

    private fun clearAll() {
        itemList.clear()
        itemListFiltered.clear()
    }

    open class ItemViewHolder<T, VM : RecyclerViewItemViewModel<T>>(itemView: View, private val viewModel: VM?, private val viewDataBinding: ViewDataBinding?) : RecyclerView.ViewHolder(itemView) {

        fun setItem(item: T) {
            viewModel?.setItem(item)
            viewDataBinding?.executePendingBindings()
        }

    }

    // filter

    fun getFilteredResults(constraint: String): List<T> {
        val results = ArrayList<T>()
        for (item in itemListFiltered) {
            if (itemFilterable(item, constraint)) {
                results.add(item)
            }
        }
        return results
    }

    abstract fun itemFilterable(item: T, constraint: String): Boolean

    private fun isFiltering(): Boolean {
        val constraint = filter.getConstraint()
        return constraint != null && constraint.isNotEmpty()
    }

    inner class AdapterFilter : Filter() {

        private var constraint: String? = null
        private var listener: FilterListener? = null

        fun filter() {
            if (listener == null) {
                this.filter(constraint)
            } else {
                listener?.let { this.filter(constraint, it) }
            }
        }

        private fun filter(constraint: String?) {
            this.constraint = constraint
            super.filter(constraint)
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            val resultList = if (results.values is List<*>) results.values else ArrayList<FilterResults>()
            itemList.clear()
            itemList.addAll(resultList as Collection<T>)
            notifyDataSetChanged()
        }

        override fun performFiltering(constraint: CharSequence): FilterResults {
            this.constraint = constraint.toString()
            val results = FilterResults()
            results.values = if (constraint.isEmpty()) ArrayList(itemListFiltered) else getFilteredResults(constraint.toString().toLowerCase(Locale.getDefault()))
            return results
        }

        fun getConstraint(): CharSequence? {
            return constraint
        }
    }

}
