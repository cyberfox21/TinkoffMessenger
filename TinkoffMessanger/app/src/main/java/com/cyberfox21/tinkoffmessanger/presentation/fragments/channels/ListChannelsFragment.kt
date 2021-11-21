package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cyberfox21.tinkoffmessanger.databinding.FragmentListChannelsBinding
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.ChannelDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.MainChannelsRecyclerAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.TopicDelegateAdapter

class ListChannelsFragment : Fragment() {

    private var selectedChannelName: String = ""

    private lateinit var fragmentCategory: Category
    private lateinit var channelsViewModel: ChannelsViewModel

    private val mainAdapter = MainChannelsRecyclerAdapter()

    private var _binding: FragmentListChannelsBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentListChannelsBinding = null")

    private var onTopicSelected: OnTopicSelected? = null

    interface OnTopicSelected {
        fun showMatchingChat(topicName: String, channelName: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListChannelsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setupRecyclerView()
        observeViewModel()
        addListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        channelsViewModel.selectedChannelName = selectedChannelName
    }

    private fun parseArguments() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_CATEGORY)) throw RuntimeException("Param category is absent")
        val category = args.get(EXTRA_CATEGORY) as Category
        if (category != Category.SUBSCRIBED && category != Category.ALL)
            throw RuntimeException("Unknown category $category")
        fragmentCategory = category
    }

    private fun setViewModel() {
        Log.d("ListChannelsFragment", "setViewModel()")
        channelsViewModel = ViewModelProvider(this)[ChannelsViewModel::class.java]
        channelsViewModel.searchChannels(fragmentCategory, INITIAL_QUERY)
    }

    private fun setupRecyclerView() {
        mainAdapter.apply {
            addDelegate(ChannelDelegateAdapter(object :
                ChannelDelegateAdapter.OnChannelDelegateClickListener {
                override fun onChannelClick(
                    channelId: Int,
                    channelName: String,
                    isSelected: Boolean
                ) {
                    selectedChannelName =
                        if (selectedChannelName != channelName) channelName else ""
                    channelsViewModel.updateTopics(channelId, isSelected)
                }

            }))
            addDelegate(TopicDelegateAdapter(object :
                TopicDelegateAdapter.OnTopicDelegateClickListener {
                override fun onTopicClick(topicName: String) {
                    onTopicSelected?.showMatchingChat(selectedChannelName, topicName)
                }
            }))
        }
        binding.categoryChannelsRecycler.adapter = mainAdapter
    }

    private fun observeViewModel() {
        channelsViewModel.selectedChannelName?.let { selectedChannelName = it }
        channelsViewModel.channelsScreenState.observe(viewLifecycleOwner, {
            processChannelsScreenState(it)
        })
    }

    private fun processChannelsScreenState(state: ChannelsScreenState) {
        when (state) {
            is ChannelsScreenState.Result -> {
                mainAdapter.submitList(state.items)
                binding.pbLoading.isVisible = false
            }
            ChannelsScreenState.Loading -> binding.pbLoading.isVisible = true
            is ChannelsScreenState.Error -> {
                Toast.makeText(this.context, "${state.error.message}", Toast.LENGTH_SHORT).show()
                binding.pbLoading.isVisible = false
            }
        }
    }

    private fun addListeners() {
        setFragmentResultListener(
            ChannelsFragment.SEARCH_QUERY
        ) { key, bundle ->
            channelsViewModel.searchChannels(
                fragmentCategory, bundle.getString(key) ?: ""
            )
        }
    }

    companion object {
        private const val EXTRA_CATEGORY = "extra_category"
        private const val INITIAL_QUERY: String = ""

        fun newInstance(
            category: Category,
            onTopicSelected: OnTopicSelected
        ): ListChannelsFragment {

            return ListChannelsFragment().apply {
                this.onTopicSelected = onTopicSelected
                arguments = Bundle().apply {
                    putSerializable(EXTRA_CATEGORY, category)
                }
            }
        }
    }

}
