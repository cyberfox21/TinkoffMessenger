package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.reactions

import androidx.recyclerview.widget.DiffUtil
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction

class ReactionDiffUtilCallback : DiffUtil.ItemCallback<Reaction>() {
    override fun areItemsTheSame(oldItem: Reaction, newItem: Reaction): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Reaction, newItem: Reaction): Boolean {
        return oldItem == newItem
    }
}
