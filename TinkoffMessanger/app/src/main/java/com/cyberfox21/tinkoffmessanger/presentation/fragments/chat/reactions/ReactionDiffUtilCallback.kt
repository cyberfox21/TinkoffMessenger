package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.reactions

import androidx.recyclerview.widget.DiffUtil

class ReactionDiffUtilCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
