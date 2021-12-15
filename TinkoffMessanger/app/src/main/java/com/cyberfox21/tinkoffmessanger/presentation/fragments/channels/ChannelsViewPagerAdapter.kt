package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ChannelsViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var categories = listOf<Category>()
    private var fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int) = fragments[position]

    fun setCategoryList(list: List<Category>) {
        categories = list
        categories.forEach {
            fragments.add(ListChannelsFragment.newInstance(it))
        }
        notifyItemRangeChanged(LIST_START_POSITION, categories.size)
    }

    companion object {
        const val LIST_START_POSITION = 0
    }
}
