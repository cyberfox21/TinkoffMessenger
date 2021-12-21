package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.recycler


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.cyberfox21.tinkoffmessanger.databinding.ItemUserBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.User

class PeopleRecyclerAdapter : ListAdapter<User, UserViewHolder>(UserDIffUtilCallback()) {

    var onPersonClickListener: OnPersonClickListener? = null

    interface OnPersonClickListener {
        fun onPersonClick(user: User)
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
            Glide.with(binding.root).load(user.avatar).into(ivUserAvatar)
            tvUserName.text = user.name
            tvUserEmail.text = user.email
            root.setOnClickListener {
                onPersonClickListener?.onPersonClick(user)
            }
        }
    }
}
