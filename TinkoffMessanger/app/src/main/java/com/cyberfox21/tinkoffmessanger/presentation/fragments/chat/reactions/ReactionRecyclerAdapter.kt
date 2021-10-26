package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.reactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cyberfox21.tinkoffmessanger.databinding.ItemEmojiBinding

class ReactionRecyclerAdapter :
    ListAdapter<String, ReactionViewHolder>(ReactionDiffUtilCallback()) {

    var onEmojiDialogClickListener: OnEmojiDialogClickListener? = null

    interface OnEmojiDialogClickListener {
        fun onEmojiDialogClick(emoji: String)
    }

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
        val binding = holder.binding
        with(binding) {
            tvEmoji.text = currentList[position]
            root.setOnClickListener {
                onEmojiDialogClickListener?.onEmojiDialogClick(currentList[position])
            }
        }

    }

}
