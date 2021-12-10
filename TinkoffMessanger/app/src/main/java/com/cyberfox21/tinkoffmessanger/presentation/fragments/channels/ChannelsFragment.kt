package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentChannelsBinding
import com.cyberfox21.tinkoffmessanger.presentation.NavigationHolder
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.ChatFragment
import com.google.android.material.tabs.TabLayoutMediator

class ChannelsFragment : Fragment(), ListChannelsFragment.OnTopicSelected {

    private var _binding: FragmentChannelsBinding? = null
    private val binding: FragmentChannelsBinding
        get() = _binding ?: throw RuntimeException("FragmentChannelsBinding = null")

    private lateinit var searchView: SearchView

    private lateinit var vpAdapter: ChannelsViewPagerAdapter

    private val tabs = listOf(Category.SUBSCRIBED.uiName, Category.ALL.uiName)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ChannelsFragment", "onCreateView()")
        _binding = FragmentChannelsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ChannelsFragment", "onViewCreated()")
        setupStatusBar()
        setupNavigation()
        setupViewPager()
        setupTabLayout()
        setupSearchPanel()
    }

    private fun setupStatusBar() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_navigation_background)
    }

    private fun setupNavigation() {
        (activity as NavigationHolder).showNavigation()
    }

    override fun onStart() {
        super.onStart()
        setupNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ChannelsFragment", "onDestroyView()")
        _binding = null
    }

    private fun setupViewPager() {
        val categories = listOf(Category.SUBSCRIBED, Category.ALL)
        vpAdapter = ChannelsViewPagerAdapter(childFragmentManager, lifecycle, this)
        vpAdapter.setCategoryList(categories)
        binding.vpCategories.adapter = vpAdapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.vpCategories) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    private fun setupSearchPanel() {
        binding.toolbarLayout.toolbar.setTitleTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.toolbarLayout.toolbar.title = getString(R.string.channels)
        val searchMenuItem = binding.toolbarLayout.toolbar.menu.findItem(R.id.actionSearch)
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = searchMenuItem.actionView as SearchView
        searchMenuItem.isVisible = true
        searchView.apply {
            findViewById<ImageView>(R.id.search_close_btn).setImageResource(R.drawable.ic_close)
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setIconifiedByDefault(false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
//                    DisplayUtils.hideKeyboard(activity)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
//                    onQueryChanged(newText)
                    activity?.supportFragmentManager?.setFragmentResult(
                        SEARCH_QUERY,
                        Bundle().apply { putString(QUERY, newText) })
                    return true
                }

            })
        }
    }

    override fun showMatchingChat(chnlName: String, topicName: String) {
        (requireActivity() as NavigationHolder).startFragment(
            ChatFragment.newInstance(
                chnlName,
                topicName
            ),
            CHANNELS_FRAGMENT_NAME
        )
    }


    companion object {
        const val CHANNELS_FRAGMENT_NAME = "channels_fragment"
        const val SEARCH_QUERY = "search_query"
        const val QUERY = "query"
        fun newInstance(): ChannelsFragment {
            return ChannelsFragment()
        }
    }

}
