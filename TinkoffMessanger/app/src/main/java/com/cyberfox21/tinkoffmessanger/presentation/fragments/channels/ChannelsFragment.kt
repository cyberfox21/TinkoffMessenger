package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentChannelsBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.domain.enums.Category
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.ChatFragment
import com.google.android.material.tabs.TabLayoutMediator

class ChannelsFragment : Fragment(), ListChannelsFragment.OnTopicSelected {

    private var _binding: FragmentChannelsBinding? = null
    private val binding: FragmentChannelsBinding
        get() = _binding ?: throw RuntimeException("FragmentChannelsBinding = null")

    private val tabs = listOf(Category.SUBSCRIBED.uiName, Category.ALL.uiName)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewPager() {
        val categories = listOf(Category.SUBSCRIBED, Category.ALL)
        val vpAdapter = ChannelsViewPagerAdapter(parentFragmentManager, lifecycle, this)
        vpAdapter.setCategoryList(categories)
        binding.vpCategories.adapter = vpAdapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.vpCategories) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    override fun showMatchingChat(topic: Topic) {
        parentFragmentManager.beginTransaction()
            .addToBackStack(CHANNELS_FRAGMENT_NAME)
            .add(
                R.id.main_fragment_container,
                ChatFragment.newInstance(topic)
            )
            .commit()
    }

    companion object {
        const val CHANNELS_FRAGMENT_NAME = "channels_fragment"
        fun newInstance(): ChannelsFragment {
            return ChannelsFragment()
        }
    }

}
