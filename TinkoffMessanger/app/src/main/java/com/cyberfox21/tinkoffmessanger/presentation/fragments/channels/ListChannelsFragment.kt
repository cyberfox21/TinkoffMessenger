package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentListChannelsBinding
import com.cyberfox21.tinkoffmessanger.presentation.MainActivity
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.ChannelsFragment.Companion.QUERY
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.SpacesItemDecoration
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.ChannelDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.MainChannelsRecyclerAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.TopicDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.ChannelsState.Companion.UNDEFINED_CHANNEL_ID
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.ChannelsState.Companion.UNDEFINED_CHANNEL_NAME
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ListChannelsFragment : ElmFragment<ChannelsEvent, ChannelsEffect, ChannelsState>() {

    private lateinit var fragmentCategory: Category

    private val mainAdapter = MainChannelsRecyclerAdapter()

    private var _binding: FragmentListChannelsBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentListChannelsBinding = null")

    private var onTopicSelected: OnTopicSelected? = null

    interface OnTopicSelected {
        fun showMatchingChat(topicName: String, channelName: String)
    }

//  < ---------------------------------------- ELM --------------------------------------------->

    @Inject
    internal lateinit var actor: ChannelsActor

    override fun createStore(): Store<ChannelsEvent, ChannelsEffect, ChannelsState> =
        ChannelsStoreFactory(actor).provide()

    override val initEvent: ChannelsEvent = ChannelsEvent.Ui.GetChannelsList(
        INITIAL_QUERY, Category.SUBSCRIBED
    )

    override fun render(state: ChannelsState) {
        with(binding) {
            channelsShimmerLayout.shimmerViewContainer.isVisible =
                state.isFirstLoading && state.error == null
            emptyLayout.errorLayout.isVisible = state.isEmptyState
            networkErrorLayout.errorLayout.isVisible = state.error != null
            categoryChannelsRecycler.isVisible = state.isEmptyState.not() && state.error == null
            mainAdapter.submitList(state.delegateItems)
        }
    }

    override fun handleEffect(effect: ChannelsEffect) {
        when (effect) {
            ChannelsEffect.EmptyChannels -> {
                binding.categoryChannelsRecycler.isVisible = false
                binding.networkErrorLayout.errorLayout.isVisible = false
                binding.emptyLayout.errorLayout.isVisible = true
            }
            is ChannelsEffect.ChannelsLoadError -> {
                binding.categoryChannelsRecycler.isVisible = false
                binding.emptyLayout.errorLayout.isVisible = false
                binding.networkErrorLayout.errorLayout.isVisible = true
            }
            ChannelsEffect.EmptyTopics -> {
                //TODO
            }
            is ChannelsEffect.TopicsLoadError -> {
                //TODO
            }
            is ChannelsEffect.RefrashTopics -> {
                mainAdapter.submitList(effect.items)
            }
        }
    }

//  < ---------------------------------------- ELM --------------------------------------------->

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
                    store.currentState.selectedChannelId,
                    isSelected = true
                )
            )
        }
    }

    private fun setupRecyclerView() {
        binding.categoryChannelsRecycler.setHasFixedSize(true)
        binding.categoryChannelsRecycler.addItemDecoration(
            SpacesItemDecoration(
                resources.getDimensionPixelOffset(
                    R.dimen.itemDecorationSize
                )
            )
        )
        mainAdapter.apply {
            addDelegate(ChannelDelegateAdapter(object :
                ChannelDelegateAdapter.OnChannelDelegateClickListener {
                override fun onChannelClick(
                    channelId: Int,
                    channelName: String,
                    isSelected: Boolean
                ) {
                    if (store.currentState.selectedChannelName != channelName) {
                        store.currentState.selectedChannelName = channelName
                        store.currentState.selectedChannelId = channelId
                    } else {
                        store.currentState.selectedChannelName = UNDEFINED_CHANNEL_NAME
                        store.currentState.selectedChannelId = UNDEFINED_CHANNEL_ID
                    }
                    if (!isSelected) {
                        store.accept(
                            ChannelsEvent.Ui.GetChannelsList(
                                INITIAL_QUERY,
                                fragmentCategory
                            )
                        )
                        store.accept(ChannelsEvent.Ui.UpdateTopics(channelId, isSelected))
                    } else {
                        store.accept(
                            ChannelsEvent.Ui.GetChannelsList(
                                INITIAL_QUERY,
                                fragmentCategory
                            )
                        )
                    }
                }

            }))
            addDelegate(TopicDelegateAdapter(object :
                TopicDelegateAdapter.OnTopicDelegateClickListener {
                override fun onTopicClick(topicName: String) {
                    onTopicSelected?.showMatchingChat(
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
        binding.networkErrorLayout.networkButton.setOnClickListener {
            store.accept(ChannelsEvent.Ui.GetChannelsList(INITIAL_QUERY, fragmentCategory))
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
