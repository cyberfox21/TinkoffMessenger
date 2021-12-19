package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentListChannelsBinding
import com.cyberfox21.tinkoffmessanger.presentation.common.MainActivity
import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.ChannelsFragment.Companion.QUERY
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.SpacesItemDecoration
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.ChannelDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.MainChannelsRecyclerAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.TopicDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.ChannelsState.Companion.UNDEFINED_CHANNEL_NAME
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.enums.Category
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ListChannelsFragment : ElmFragment<ChannelsEvent, ChannelsEffect, ChannelsState>() {

    private lateinit var fragmentCategory: Category

    private val mainAdapter = MainChannelsRecyclerAdapter()

    private var _binding: FragmentListChannelsBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentListChannelsBinding = null")

//  < ---------------------------------------- ELM --------------------------------------------->

    @Inject
    internal lateinit var actor: ChannelsActor

    override fun createStore(): Store<ChannelsEvent, ChannelsEffect, ChannelsState> =
        ChannelsStoreFactory(actor).provide()

    override val initEvent: ChannelsEvent = ChannelsEvent.Ui.GetChannelsList(
        INITIAL_QUERY, Category.SUBSCRIBED
    )

    override fun render(state: ChannelsState) {
        when (state.channelStatus) {
            ResourceStatus.SUCCESS -> provideSuccess(state.delegateItems)
            ResourceStatus.LOADING -> provideLoading()
            ResourceStatus.EMPTY -> provideEmpty()
            ResourceStatus.ERROR -> provideError()

        }
    }

    override fun handleEffect(effect: ChannelsEffect) {
        when (effect) {
            ChannelsEffect.Loading -> {
                binding.errorLayout.errorRoot.isVisible = false
                binding.emptyLayout.errorLayout.isVisible = false
            }
            ChannelsEffect.EmptyChannels -> {
                binding.categoryChannelsRecycler.isVisible = false
                binding.errorLayout.errorRoot.isVisible = false
                binding.emptyLayout.errorLayout.isVisible = true
            }
            is ChannelsEffect.ChannelsLoadError -> {
                binding.categoryChannelsRecycler.isVisible = false
                binding.emptyLayout.errorLayout.isVisible = false
                binding.errorLayout.errorRoot.isVisible = true
            }
            ChannelsEffect.EmptyTopics -> {
                //TODO
            }
            is ChannelsEffect.TopicsLoadError -> {
                //TODO
            }
            is ChannelsEffect.RefreshTopics -> {
                mainAdapter.submitList(effect.items)
            }
        }
    }

//  < ---------------------------------------- ELM --------------------------------------------->

    private fun provideSuccess(items: List<DelegateItem>) {
        binding.channelsShimmerLayout.shimmerViewContainer.isVisible = false
        binding.errorLayout.errorRoot.isVisible = false
        binding.emptyLayout.errorLayout.isVisible = false
        binding.categoryChannelsRecycler.isVisible = true
        mainAdapter.submitList(items)
    }

    private fun provideLoading() {
        binding.channelsShimmerLayout.shimmerViewContainer.isVisible = true
        binding.errorLayout.errorRoot.isVisible = false
        binding.emptyLayout.errorLayout.isVisible = false
        binding.categoryChannelsRecycler.isVisible = false
    }

    private fun provideEmpty() {
        binding.channelsShimmerLayout.shimmerViewContainer.isVisible = false
        binding.errorLayout.errorRoot.isVisible = false
        binding.emptyLayout.errorLayout.isVisible = true
        binding.categoryChannelsRecycler.isVisible = false
    }

    private fun provideError() {
        binding.channelsShimmerLayout.shimmerViewContainer.isVisible = false
        binding.errorLayout.errorRoot.isVisible = true
        binding.emptyLayout.errorLayout.isVisible = false
        binding.categoryChannelsRecycler.isVisible = false
    }

    override fun onAttach(context: Context) {
        (activity as MainActivity).component.injectChannelsFragment(this)
        super.onAttach(context)
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
        setupRecyclerView()
        addListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArguments() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_CATEGORY)) throw RuntimeException("Param category is absent")
        val category = args.get(EXTRA_CATEGORY) as Category
        if (category != Category.SUBSCRIBED && category != Category.ALL)
            throw RuntimeException("Unknown category $category")
        fragmentCategory = category
        launchRightTab()
    }

    private fun launchRightTab() {
        when (store.currentState.selectedChannelName) {
            UNDEFINED_CHANNEL_NAME -> store.accept(
                ChannelsEvent.Ui.GetChannelsList(
                    INITIAL_QUERY,
                    fragmentCategory
                )
            )
            else -> store.accept(
                ChannelsEvent.Ui.UpdateTopics(
                    channelId = store.currentState.selectedChannelId,
                    channelName = store.currentState.selectedChannelName,
                    isSelected = true
                )
            )
        }
    }

    private fun setupRecyclerView() {
        binding.categoryChannelsRecycler.setHasFixedSize(true)
        binding.categoryChannelsRecycler.addItemDecoration(
            SpacesItemDecoration(resources.getDimensionPixelOffset(R.dimen.itemDecorationSize))
        )
        mainAdapter.apply {
            addDelegate(ChannelDelegateAdapter(object :
                ChannelDelegateAdapter.OnChannelDelegateClickListener {
                override fun onChannelClick(
                    channelId: Int,
                    channelName: String,
                    isSelected: Boolean
                ) {
                    updateTopics(channelId, channelName, isSelected)
                }
            }))
            addDelegate(TopicDelegateAdapter(object :
                TopicDelegateAdapter.OnTopicDelegateClickListener {
                override fun onTopicClick(topicName: String) {
                    (parentFragment as ChannelsFragment).showMatchingChat(
                        store.currentState.selectedChannelName,
                        topicName
                    )
                }
            }))
        }
        binding.categoryChannelsRecycler.adapter = mainAdapter
    }

    private fun addListeners() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            fragmentCategory.uiName,
            viewLifecycleOwner
        ) { key, bundle ->
            if (key == fragmentCategory.uiName) {
                val str = bundle.getString(QUERY)
                str?.let { store.accept(ChannelsEvent.Ui.GetChannelsList(it, fragmentCategory)) }
            }
        }
        binding.errorLayout.btnNetwork.setOnClickListener {
            store.accept(ChannelsEvent.Ui.GetChannelsList(INITIAL_QUERY, fragmentCategory))
        }
        binding.emptyLayout.btnRefresh.setOnClickListener {
            store.accept(ChannelsEvent.Ui.GetChannelsList(INITIAL_QUERY, fragmentCategory))
        }
    }

    private fun updateTopics(channelId: Int, channelName: String, isSelected: Boolean) {
        store.accept(ChannelsEvent.Ui.UpdateTopics(channelId, channelName, isSelected))
    }

    companion object {
        private const val EXTRA_CATEGORY = "extra_category"
        private const val INITIAL_QUERY: String = ""

        fun newInstance(
            category: Category
        ): ListChannelsFragment {

            return ListChannelsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_CATEGORY, category)
                }
            }
        }
    }

}
