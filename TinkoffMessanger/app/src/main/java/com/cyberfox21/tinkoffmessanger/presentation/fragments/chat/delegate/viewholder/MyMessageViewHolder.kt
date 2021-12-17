package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.FlexBoxLayout
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.MyMessage
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.MyMessageViewGroup

class MyMessageViewHolder(val myMessageViewGroup: MyMessageViewGroup) :
    RecyclerView.ViewHolder(myMessageViewGroup) {

    val myMessage = myMessageViewGroup.getChildAt(0) as MyMessage
    val emojiLayout = myMessageViewGroup.getChildAt(1) as FlexBoxLayout

}
