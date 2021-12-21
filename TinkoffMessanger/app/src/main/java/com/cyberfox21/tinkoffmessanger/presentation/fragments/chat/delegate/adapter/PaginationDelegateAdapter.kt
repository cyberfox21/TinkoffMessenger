package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.MainRecyclerAdapter

class PaginationDelegateAdapter(private val paginationHelperAdapter: PaginationHelperAdapter) :
    MainRecyclerAdapter() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        paginationHelperAdapter.onBindViewHolder(position, itemCount)
    }
}