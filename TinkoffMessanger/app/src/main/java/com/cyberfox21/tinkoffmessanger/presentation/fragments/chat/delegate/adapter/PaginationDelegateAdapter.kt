package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.MainRecyclerAdapter

class PaginationDelegateAdapter(private val paginationCallback: (position: Int) -> Unit) :
    MainRecyclerAdapter() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position > itemCount - NEXT_PAGE) paginationCallback(position)
    }

    companion object {
        const val NEXT_PAGE = 5
    }
}