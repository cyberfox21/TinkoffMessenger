package com.cyberfox21.tinkoffmessanger.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cyberfox21.tinkoffmessanger.databinding.ItemEmojiBinding

class ReactionRecyclerAdapter :
    ListAdapter<String, ReactionViewHolder>(ReactionDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        return ReactionViewHolder(
            ItemEmojiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        holder.binding.tvEmoji.text = currentList[position]
    }

}
