package com.cyberfox21.tinkoffmessanger.presentation.commondelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

open class MainRecyclerAdapter :
    ListAdapter<DelegateItem, RecyclerView.ViewHolder>(DelegateAdapterItemCallback()) {

    private val delegates = mutableListOf<AdapterDelegate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType >= 0 && viewType < delegates.size) {
            delegates[viewType].onCreateViewHolder(parent)
        } else {
            EmptyViewHolder(parent.context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            delegates[getItemViewType(position)]
                .onBindViewHolder(holder, currentList[position], position)
        } catch (e: IndexOutOfBoundsException) {
            throw RuntimeException(
                "No delegate for item at position $position: ${currentList[position]}"
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return delegates.indexOfFirst { it.isOfViewType(currentList[position]) }
    }

    fun addDelegate(delegate: AdapterDelegate) {
        delegates.add(delegate)
    }
}
