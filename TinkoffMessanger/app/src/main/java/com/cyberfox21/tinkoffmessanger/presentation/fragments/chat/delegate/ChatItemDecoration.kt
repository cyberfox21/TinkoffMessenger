package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ChatItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = space

        if (parent.getChildAdapterPosition(view) == START_POSITION) outRect.bottom = space
    }

    companion object {
        const val START_POSITION = 0
    }
}
