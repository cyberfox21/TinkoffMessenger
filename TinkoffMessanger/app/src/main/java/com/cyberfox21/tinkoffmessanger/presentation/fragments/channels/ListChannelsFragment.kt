package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cyberfox21.tinkoffmessanger.databinding.FragmentListChannelsBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.domain.enums.Category
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.ChannelDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.MainChannelsRecyclerAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.TopicDelegateAdapter
import com.cyberfox21.tinkoffmessanger.util.toDelegateChannelItemsList

class ListChannelsFragment : Fragment() {

    var savedPos = DELEGATE_MAPPER_START_POSITION
    var currentType: Channel? = null

    private lateinit var fragmentCategory: Category
    private lateinit var channelsViewModel: ChannelsViewModel

    private val mainAdapter = MainChannelsRecyclerAdapter()

    private var _binding: FragmentListChannelsBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentListChannelsBinding = null")

    private var onTopicSelected: OnTopicSelected? = null

    interface OnTopicSelected {
        fun showMatchingChat(topic: Topic)
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
    }

    private fun setViewModel() {
        channelsViewModel = ViewModelProvider(this)[ChannelsViewModel::class.java]
        channelsViewModel.getList(fragmentCategory) // load right list
    }

    private fun setupRecyclerView() {
        mainAdapter.apply {
            addDelegate(ChannelDelegateAdapter(object :
                ChannelDelegateAdapter.OnChannelDelegateClickListener {
                override fun onChannelClick(item: Channel, position: Int) {
                    if (currentType != null && currentType!!.name == item.name) {
                        mainAdapter.submitList(
                            channelsViewModel.channelsList.toDelegateChannelItemsList(
                                DELEGATE_MAPPER_START_POSITION
                            )
                        )
                        currentType = null
                        savedPos = DELEGATE_MAPPER_START_POSITION
                    } else {
                        mainAdapter.submitList(
                            channelsViewModel.channelsList.toDelegateChannelItemsList(
                                if (savedPos < position) position - (currentType?.listOfTopics?.size
                                    ?: 0) else position
                            )
                        )
                        savedPos = position
                        currentType = item
                    }
                }

            }))
            addDelegate(TopicDelegateAdapter(object :
                TopicDelegateAdapter.OnTopicDelegateClickListener {
                override fun onTopicClick(item: Topic, position: Int) {
                    onTopicSelected?.showMatchingChat(item)
                }
            }))
        }
        binding.categoryChannelsRecycler.adapter = mainAdapter
    }

    private fun observeViewModel() {
        channelsViewModel.searchChannels(fragmentCategory)
        channelsViewModel.channelsScreenState.observe(viewLifecycleOwner, {
            processChannelsScreenState(it)
        })
    }

    private fun processChannelsScreenState(state: ChannelsScreenState) {
        when (state) {
            is ChannelsScreenState.Result -> {
                mainAdapter.submitList(
                    state.items.toDelegateChannelItemsList(
                        DELEGATE_MAPPER_START_POSITION
                    )
                )
                binding.pbLoading.isVisible = false
            }
            ChannelsScreenState.Loading -> binding.pbLoading.isVisible = true
            is ChannelsScreenState.Error -> {
                Toast.makeText(this.context, "${state.error.message}", Toast.LENGTH_SHORT).show()
                binding.pbLoading.isVisible = false
            }
        }
    }

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
        const val DELEGATE_MAPPER_START_POSITION = -1

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
