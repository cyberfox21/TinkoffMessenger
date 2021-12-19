package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.viewholder

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.AlienMessage
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.AlienMessageViewGroup
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.FlexBoxLayout

class AlienMessageViewHolder(val alienMessageViewGroup: AlienMessageViewGroup) :
    RecyclerView.ViewHolder(alienMessageViewGroup) {

    val imageView = alienMessageViewGroup.getChildAt(0) as ImageView
    val alienMessage = alienMessageViewGroup.getChildAt(1) as AlienMessage
    val emojiLayout = alienMessageViewGroup.getChildAt(2) as FlexBoxLayout

}
