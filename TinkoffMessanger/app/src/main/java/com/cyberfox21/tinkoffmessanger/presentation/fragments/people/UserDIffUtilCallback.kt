package com.cyberfox21.tinkoffmessanger.presentation.fragments.people

import androidx.recyclerview.widget.DiffUtil
import com.cyberfox21.tinkoffmessanger.domain.entity.User

class UserDIffUtilCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}
