package com.fullsekurity.theatreblood.recyclerview

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView


abstract class RecyclerViewViewModel(application: Application) : AndroidViewModel(application) {

    abstract val adapter: RecyclerView.Adapter<*>

    abstract val itemDecorator: RecyclerView.ItemDecoration?

    abstract fun setLayoutManager(): RecyclerView.LayoutManager

    fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = setLayoutManager()
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView.setItemViewCacheSize(50)
        itemDecorator?.let {
            if (recyclerView.itemDecorationCount == 0) {
                recyclerView.addItemDecoration(it)
            }
        }
        recyclerView.adapter = adapter
    }

}
