package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.reactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cyberfox21.tinkoffmessanger.databinding.ItemEmojiBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction

class ReactionListAdapter :
    ListAdapter<Reaction, ReactionViewHolder>(ReactionDiffUtilCallback()) {

    var onEmojiListListener: OnEmojiListListener? = null

    interface OnEmojiListListener {
        fun onEmojiClick(emoji: Reaction)
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
            tvEmoji.text = currentList[position].reaction
            root.setOnClickListener {
                onEmojiListListener?.onEmojiClick(currentList[position])
            }
        }

    }

}
