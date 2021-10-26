package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate

import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.databinding.ItemChannelBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel

class ChannelViewHolder(
    val binding: ItemChannelBinding,
    private val onChannelClick: ChannelDelegateAdapter.OnChannelDelegateClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Channel) {
        binding.tvChannelTitle.text = item.name
        binding.root.setOnClickListener {
            //binding.ivExpandChannel.setImageResource()
            onChannelClick.onChannelClick(item, adapterPosition)
        }
    }
}
