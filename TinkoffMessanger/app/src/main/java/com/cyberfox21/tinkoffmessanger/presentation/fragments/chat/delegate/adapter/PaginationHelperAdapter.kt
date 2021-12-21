package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

class PaginationHelperAdapter(private val paginationCallback: (position: Int) -> Unit) {
    fun onBindViewHolder(position: Int, itemCount: Int) {
        if (position > itemCount - NEXT_PAGE) paginationCallback(position)
    }

    companion object {
        const val NEXT_PAGE = 5
    }
}