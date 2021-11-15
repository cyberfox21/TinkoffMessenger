package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.FlexBoxLayout
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.MyMessageViewGroup

class MyMessageViewHolder(val myMessageViewGroup: MyMessageViewGroup) :
    RecyclerView.ViewHolder(myMessageViewGroup) {

    val time = myMessageViewGroup.getChildAt(0) as TextView
    val text = myMessageViewGroup.getChildAt(1) as TextView
    val emojiLayout = myMessageViewGroup.getChildAt(2) as FlexBoxLayout

}