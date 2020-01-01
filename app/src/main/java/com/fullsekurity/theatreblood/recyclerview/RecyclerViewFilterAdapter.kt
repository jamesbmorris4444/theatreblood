package com.fullsekurity.theatreblood.recyclerview

import android.content.Context
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewFilterAdapter<T, VM : RecyclerViewItemViewModel<T>>(var context: Context) :  RecyclerView.Adapter<RecyclerViewFilterAdapter.ItemViewHolder<T, VM>>(), Filterable {

    var recyclerView: RecyclerView? = null
    private var itemsFilter: ItemsFilter? = null
    val itemList: MutableList<T> = mutableListOf()
    private val unfilteredList: MutableList<T> = mutableListOf()

    private val filter: ItemsFilter
        get() {
            if (itemsFilter == null) {
                itemsFilter = ItemsFilter()
            }
            return itemsFilter ?: ItemsFilter()
        }

    override fun onBindViewHolder(holder: ItemViewHolder<T, VM>, position: Int) {
        holder.setItem(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addAll(toAddList: List<T>) {
        clearAll()
        unfilteredList.addAll(toAddList)
        if (filtering()) {
            itemsFilter!!.filter()
        } else {
            itemList.addAll(toAddList)
            if (recyclerView != null) {
                recyclerView!!.recycledViewPool.clear()
            }
            notifyDataSetChanged()
        }
    }

    fun remove(position: Int) {
        if (itemCount > position) {
            itemList.removeAt(position)
            unfilteredList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun contains(item: T, position: Int): Boolean {
        return getItem(position) == item
    }

    fun getItem(position: Int): T {
        return itemList[position]
    }

    fun clearAll() {
        itemList.clear()
        unfilteredList.clear()
    }

    fun itemFilterable(item: T, constraint: String): Boolean {
        return item.toString().toLowerCase().contains(constraint)
    }

    fun getFilteredResults(constraint: String): List<T> {
        val results = ArrayList<T>()
        for (item in unfilteredList) {
            if (itemFilterable(item, constraint)) {
                results.add(item)
            }
        }
        return results
    }

    fun filter(constraint: String, listener: Filter.FilterListener) {
        filter.filter(constraint, listener)
    }

    private fun filtering(): Boolean {
        val constraint = filter.getConstraint()
        return constraint != null && constraint.length != 0
    }

    open class ItemViewHolder<T, VM : RecyclerViewItemViewModel<T>>(
        itemView: View,
        val viewModel: VM?,
        val viewDataBinding: ViewDataBinding?
    ) : RecyclerView.ViewHolder(itemView) {

        fun setItem(item: T) {
            viewModel?.setItem(item)
            viewDataBinding?.executePendingBindings()
        }
    }

    inner class ItemsFilter : Filter() {

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

        fun filter(constraint: String?, listener: FilterListener) {
            this.listener = listener
            this.constraint = constraint
            super.filter(constraint, listener)
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {

            val resultList = if (results.values is List<*>) results.values else ArrayList<FilterResults>()
            itemList.clear()
            itemList.addAll(resultList as Collection<T>)
            if (recyclerView != null) {
                recyclerView!!.recycledViewPool.clear()
            }
            this@RecyclerViewFilterAdapter.notifyDataSetChanged()
        }

        override fun performFiltering(constraint: CharSequence): FilterResults {

            this.constraint = constraint.toString()
            val results = FilterResults()
            results.values =
                if (constraint.length == 0) ArrayList(unfilteredList) else getFilteredResults(constraint.toString().toLowerCase())
            return results
        }

        fun getConstraint(): CharSequence? {
            return constraint
        }
    }

}
