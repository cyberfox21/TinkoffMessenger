package com.cyberfox21.tinkoffmessanger.presentation.fragments.people


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cyberfox21.tinkoffmessanger.databinding.ItemUserBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.User

class PeopleRecyclerAdapter : ListAdapter<User, UserViewHolder>(UserDIffUtilCallback()) {

    var onPersonClickListener : OnPersonClickListener? = null

    interface OnPersonClickListener {
        fun onPeopleClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = currentList[position]
        val binding = holder.binding
        with(binding) {
            ivUserAvatar.setImageResource(user.avatar)
            tvUserName.text = user.name
            tvUserEmail.text = user.email
            online.visibility = getStatus(user.status)
            root.setOnClickListener {
                onPersonClickListener?.onPeopleClick()
            }
        }
    }

    private fun getStatus(status: Boolean) = when (status) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}
