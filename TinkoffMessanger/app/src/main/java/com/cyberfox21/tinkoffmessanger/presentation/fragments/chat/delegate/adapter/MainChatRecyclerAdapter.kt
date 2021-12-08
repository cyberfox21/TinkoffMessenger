package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateAdapterItemCallback
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

class MainChatRecyclerAdapter :
    ListAdapter<DelegateItem, RecyclerView.ViewHolder>(DelegateAdapterItemCallback()) {

    private val delegates = mutableListOf<AdapterDelegate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegates[getItemViewType(position)].onBindViewHolder(
            holder,
            currentList[position],
            position
        )
    }

    override fun getItemViewType(position: Int): Int {
        return delegates.indexOfFirst { it.isOfViewType(currentList[position]) }
    }

    fun addDelegate(delegate: AdapterDelegate) {
        delegates.add(delegate)
    }
}
