package com.cyberfox21.tinkoffmessanger.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageViewHolder(val emojiMessageViewGroup: EmojiMessageViewGroup) :
    RecyclerView.ViewHolder(emojiMessageViewGroup) {

    val imageView = emojiMessageViewGroup.getChildAt(0) as ImageView
    val time = emojiMessageViewGroup.getChildAt(1) as TextView
    val name = emojiMessageViewGroup.getChildAt(2) as TextView
    val text = emojiMessageViewGroup.getChildAt(3) as TextView
    val emojiLayout = emojiMessageViewGroup.getChildAt(4) as FlexBoxLayout
}