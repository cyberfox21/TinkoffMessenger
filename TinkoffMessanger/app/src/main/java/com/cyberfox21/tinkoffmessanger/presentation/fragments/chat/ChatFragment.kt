package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.BottomSheetDialogLayoutBinding
import com.cyberfox21.tinkoffmessanger.databinding.FragmentChatBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.MainActivity
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.OnLongMessageClickListener
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter.AlienMessageDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter.DateDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter.MainChatRecyclerAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter.MyMessageDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.reactions.ReactionRecyclerAdapter
import com.cyberfox21.tinkoffmessanger.presentation.toDelegateChatItemsList
import com.google.android.material.bottomsheet.BottomSheetDialog
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject


class ChatFragment : ElmFragment<ChatEvent, ChatEffect, ChatState>() {

    private lateinit var fragmentChannelName: String
    private lateinit var fragmentTopicName: String

    internal lateinit var gridLayoutManager: GridLayoutManager

    internal lateinit var bottomSheetDialog: BottomSheetDialog

    private var _binding: FragmentChatBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentChatBinding = null")

    private var _dialogLayoutBinding: BottomSheetDialogLayoutBinding? = null
    private val dialogLayoutBinding
        get() = _dialogLayoutBinding
            ?: throw RuntimeException("BottomSheetDialogLayoutBinding = null")

    private val chatRecyclerAdapter = MainChatRecyclerAdapter()
    private val reactionRecyclerAdapter = ReactionRecyclerAdapter()

    private val onLongMessageClickListener = object : OnLongMessageClickListener {
        override fun onLongMessageClick(message: DelegateItem) {
            showBottomSheetDialog(message)
        }
    }

//  < ---------------------------------------- ELM --------------------------------------------->

    @Inject
    internal lateinit var actor: ChatActor

    override val initEvent: ChatEvent = ChatEvent.Ui.GetReactionList

    override fun createStore(): Store<ChatEvent, ChatEffect, ChatState> =
        ChatStoreFactory(actor).provide()

    override fun render(state: ChatState) {
        with(binding) {
            pbLoading.isVisible = state.isMessagesLoading
            emptyLayout.errorLayout.isVisible =
                state.isEmptyMessageList && state.messages.isEmpty() && state.isMessagesLoading.not()
            if (state.messages.isNotEmpty() && state.isEmptyMessageList.not()) {
                chatRecycler.isVisible = true
                // todo get current user id
                chatRecyclerAdapter.submitList(
                    state.messages.toDelegateChatItemsList(
                        UNDEFINED_USER_ID
                    )
                )
            }
            networkErrorLayout.errorLayout.isVisible =
                state.messageError != null && state.isMessagesLoading.not()
        }
        dialogLayoutBinding.pbLoading.isVisible = state.isReactionsLoading
        dialogLayoutBinding.emojiRecycler.isVisible = state.isEmptyReactionList.not()
        reactionRecyclerAdapter.submitList(state.reactions)
    }

    override fun handleEffect(effect: ChatEffect) {
        when (effect) {
            ChatEffect.EmptyMessageList -> {
                binding.chatRecycler.isVisible = false
                binding.emptyLayout.errorLayout.isVisible = true
                binding.networkErrorLayout.errorLayout.isVisible = false
            }
            is ChatEffect.MessagesLoadError -> {
                binding.chatRecycler.isVisible = false
                binding.emptyLayout.errorLayout.isVisible = false
                binding.networkErrorLayout.errorLayout.isVisible = true
            }
            ChatEffect.EmptyReactionList -> TODO()
            is ChatEffect.ReactionsLoadError -> TODO()
        }
    }

//  < ---------------------------------------- ELM --------------------------------------------->

    override fun onAttach(context: Context) {
        (activity as MainActivity).component.injectChatFragment(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
        setActorFields()
        getReactionList()
    }

    private fun getReactionList() {
        store.accept(ChatEvent.Ui.GetMessages)
        store.accept(ChatEvent.Ui.GetReactionList)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        configureBottomNavigation()
        setupViews()
        addListeners()
        setBottomSheetDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        store.stop()
    }

    private fun parseArguments() {
        val args = requireArguments()
        if (!args.containsKey(TOPIC_EXTRA)) throw RuntimeException("Param topicName is absent")
        val topic = args.getString(TOPIC_EXTRA)
        topic?.let { fragmentTopicName = it }
        if (!args.containsKey(CHANNEL_EXTRA)) throw RuntimeException("Param channelName is absent")
        val channel = args.getString(CHANNEL_EXTRA)
        channel?.let { fragmentChannelName = it }
    }

    private fun setActorFields() {
        actor.channelName = fragmentChannelName
        actor.topicName = fragmentTopicName
    }

    private fun configureToolbar() {
        binding.toolbarLayout.root.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green
            )
        )
        binding.toolbarLayout.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbarLayout.toolbar.title = setChannel(fragmentChannelName)
        binding.toolbarLayout.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun configureBottomNavigation() {
        (activity as MainActivity).hideNavigation()
    }

    private fun setupViews() {
        setupStatusBar()
        binding.tvTopicTitle.text = setTopic(fragmentTopicName)
        chatRecyclerAdapter.addDelegate(AlienMessageDelegateAdapter(onLongMessageClickListener))
        chatRecyclerAdapter.addDelegate(MyMessageDelegateAdapter(onLongMessageClickListener))
        chatRecyclerAdapter.addDelegate(DateDelegateAdapter())
        binding.chatRecycler.setHasFixedSize(true)
        binding.chatRecycler.adapter = chatRecyclerAdapter
    }

    private fun setChannel(channel: String) = "#$channel"

    private fun setTopic(topic: String) = "Topic: #$topic"

    private fun setupStatusBar() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.green)
    }

    private fun addListeners() {
        with(binding) {
            messageFieldLayout.imageBtnSend.setOnClickListener {
//                viewModel.sendMessage(binding.etMessageField.text)
            }
            messageFieldLayout.etMessageField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    messageFieldLayout.imageBtnSend.setImageResource(getImageBtnResource(count))

                override fun afterTextChanged(s: Editable?) {}

            })
        }
//        chatRecyclerAdapter.onLongMessageClickListener =
//            object : ChatRecyclerAdapter.OnLongMessageClickListener {
//                override fun onLongMessageClick(message: Message) {
//                    showBottomSheetDialog(message)
//                }
//            }
    }

//    private fun observeViewModel() {
//        viewModel.reactionsListStateLD.observe(
//            viewLifecycleOwner,
//            { processedReactionsListState(it) })
//        viewModel.chatScreenStateLD.observe(viewLifecycleOwner, { processedChatScreenState(it) })
//    }

    private fun setBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireActivity())
        _dialogLayoutBinding = BottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogLayoutBinding.root)
        dialogLayoutBinding.emojiRecycler.layoutManager =
            GridLayoutManager(requireActivity(), 6)
        dialogLayoutBinding.emojiRecycler.adapter = reactionRecyclerAdapter
    }

    private fun showBottomSheetDialog(message: DelegateItem) {
        reactionRecyclerAdapter.onEmojiDialogClickListener =
            object : ReactionRecyclerAdapter.OnEmojiDialogClickListener {
                override fun onEmojiDialogClick(emoji: Reaction) {
//                    viewModel.addNewEmoji(message, emoji)
                    store.accept(ChatEvent.Ui.AddReaction(emoji, message))
                    Log.d("ChatActivity", "emoji selected $emoji")
                    bottomSheetDialog.dismiss()
                }
            }
        bottomSheetDialog.show()
    }

    private fun getImageBtnResource(count: Int): Int = when (count) {
        0 -> {
            R.drawable.ic_attach
        }
        else -> {
            R.drawable.ic_send
        }
    }

    companion object {

        const val CHANNEL_EXTRA = "channel_extra"
        const val TOPIC_EXTRA = "topic_extra"

        const val UNDEFINED_USER_ID = -1

        fun newInstance(channelName: String, topic: String): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(CHANNEL_EXTRA, channelName)
                    putString(TOPIC_EXTRA, topic)
                }
            }
        }
    }
}
