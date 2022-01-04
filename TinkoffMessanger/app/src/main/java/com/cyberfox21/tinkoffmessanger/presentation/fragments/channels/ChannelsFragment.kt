package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentChannelsBinding
import com.cyberfox21.tinkoffmessanger.presentation.common.NavigationHolder
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.enums.Category
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.ChatFragment
import com.google.android.material.tabs.TabLayoutMediator

class ChannelsFragment : Fragment() {

    private var _binding: FragmentChannelsBinding? = null
    private val binding: FragmentChannelsBinding
        get() = _binding ?: throw RuntimeException("FragmentChannelsBinding = null")

    private lateinit var searchView: SearchView

    private lateinit var vpAdapter: ChannelsViewPagerAdapter

    private var fragmentCurrentPagesPosition = Category.SUBSCRIBED

    private val tabs = listOf(Category.SUBSCRIBED.uiName, Category.ALL.uiName)

    private lateinit var onPageChangeCallback: ViewPager2.OnPageChangeCallback

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
        setupStatusBar()
        setupViewPager()
        setupTabLayout()
        setupSearchPanel()
    }

    private fun setupStatusBar() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_navigation_background)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.vpCategories.unregisterOnPageChangeCallback(onPageChangeCallback)
        _binding = null
    }

    private fun getCurrentPageCategory(position: Int) = when (position) {
        0 -> Category.SUBSCRIBED
        1 -> Category.ALL
        else -> throw RuntimeException("Unknown page category position $position")
    }

    private fun setupViewPager() {
        val categories = listOf(Category.SUBSCRIBED, Category.ALL)
        vpAdapter = ChannelsViewPagerAdapter(childFragmentManager, lifecycle)
        vpAdapter.setCategoryList(categories)
        onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                fragmentCurrentPagesPosition =
                    getCurrentPageCategory(binding.vpCategories.currentItem)
            }
        }
        binding.vpCategories.apply {
            registerOnPageChangeCallback(onPageChangeCallback)
            adapter = vpAdapter
        }
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.vpCategories) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    private fun setupSearchPanel() {
        binding.toolbarLayout.toolbar.setTitleTextColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        binding.toolbarLayout.toolbar.title = getString(R.string.channels)
        val searchMenuItem = binding.toolbarLayout.toolbar.menu.findItem(R.id.actionSearch)
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE)
                as SearchManager
        searchView = searchMenuItem.actionView as SearchView
        searchMenuItem.isVisible = true
        searchView.apply {
            findViewById<ImageView>(R.id.search_close_btn).setImageResource(R.drawable.ic_close)
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setIconifiedByDefault(false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    activity?.supportFragmentManager?.setFragmentResult(
                        fragmentCurrentPagesPosition.uiName,
                        Bundle().apply { putString(QUERY, newText) })
                    return true
                }
            })
        }
    }

    fun showMatchingChat(channelName: String, topicName: String) {
        (requireActivity() as NavigationHolder).startFragment(
            ChatFragment.newInstance(channelName, topicName),
            CHANNELS_FRAGMENT_NAME,
            ChatFragment.CHAT_FRAGMENT_NAME
        )
    }

    companion object {
        const val CHANNELS_FRAGMENT_NAME = "channels_fragment"
        const val QUERY = "query"
        fun newInstance(): ChannelsFragment {
            return ChannelsFragment()
        }
    }

}
