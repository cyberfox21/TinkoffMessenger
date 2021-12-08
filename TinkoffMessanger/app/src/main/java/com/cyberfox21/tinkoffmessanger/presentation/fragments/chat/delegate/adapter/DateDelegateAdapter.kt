package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.DateDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.viewholder.DateViewHolder
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.CustomDateView

class DateDelegateAdapter : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return DateViewHolder(CustomDateView(parent.context).apply { layoutParams = lp })
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        val date = (item as DateDelegateItem).content() as String
        (holder as DateViewHolder).customDateView.text = date
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is DateDelegateItem
}
