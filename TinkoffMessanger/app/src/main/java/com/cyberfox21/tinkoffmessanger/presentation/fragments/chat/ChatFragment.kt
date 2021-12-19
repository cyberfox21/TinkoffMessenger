package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.app.AlertDialog
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
import com.cyberfox21.tinkoffmessanger.databinding.SendingMessageErrorDialogLayoutBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.MainActivity
import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.SpacesItemDecoration
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.ChatDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.reactions.ReactionRecyclerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject


class ChatFragment : ElmFragment<ChatEvent, ChatEffect, ChatState>() {

    private lateinit var fragmentChannelName: String
    private lateinit var fragmentTopicName: String

    internal lateinit var bottomSheetDialog: BottomSheetDialog

    private var alertDialog: AlertDialog? = null

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
        override fun onLongMessageClick(messageId: Int) {
            showBottomSheetDialog(messageId)
        }
    }

    private val onReactionClickListener = object : OnReactionClickListener {
        override fun onReactionClick(
            clickMode: ClickEmojiMode,
            messageId: Int,
            emoji: Reaction
        ) = when (clickMode) {
            ClickEmojiMode.ADD_EMOJI -> processAddEmoji(messageId, emoji)
            ClickEmojiMode.DELETE_EMOJI -> processDeleteEmoji(messageId, emoji)
        }

    }

//  < ---------------------------------------- ELM --------------------------------------------->

    @Inject
    internal lateinit var actor: ChatActor

    override val initEvent: ChatEvent = ChatEvent.Ui.GetMessages

    override fun createStore(): Store<ChatEvent, ChatEffect, ChatState> =
        ChatStoreFactory(actor).provide()

    override fun render(state: ChatState) {
        when (state.messageStatus) {
            ResourceStatus.SUCCESS -> provideMessageSuccess(state.messages)
            ResourceStatus.LOADING -> provideMessageLoading()
            ResourceStatus.EMPTY -> provideMessageEmpty()
            ResourceStatus.ERROR -> provideMessageError()
        }

        when (state.reactionsListStatus) {
            ResourceStatus.SUCCESS -> provideReactionListSuccess(state.reactions)
            ResourceStatus.LOADING -> provideReactionListLoading()
            ResourceStatus.EMPTY -> provideReactionListEmpty()
            ResourceStatus.ERROR -> provideReactionListError()
        }
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
            ChatEffect.EmptyReactionList -> {
            }
            is ChatEffect.ReactionsLoadError -> {
            }
            ChatEffect.MessageSendingError -> showErrorDialog()
            ChatEffect.PrepareReactionList -> getReactionList()
            ChatEffect.StartChatFragmentWork -> getMessageList()
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
        getCurrentUserId()
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
        _dialogLayoutBinding = null
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
        chatRecyclerAdapter.addDelegate(
            AlienMessageDelegateAdapter(onReactionClickListener, onLongMessageClickListener)
        )
        chatRecyclerAdapter.addDelegate(
            MyMessageDelegateAdapter(onReactionClickListener, onLongMessageClickListener)
        )
        chatRecyclerAdapter.addDelegate(DateDelegateAdapter())
        binding.chatRecycler.addItemDecoration(
            SpacesItemDecoration(resources.getDimensionPixelOffset(R.dimen.messageSpaceSize))
        )
        binding.chatRecycler.setHasFixedSize(true)
        binding.chatRecycler.adapter = chatRecyclerAdapter
    }

    private fun provideMessageSuccess(messages: List<ChatDelegateItem>) {
        binding.pbLoading.isVisible = false
        binding.emptyLayout.errorLayout.isVisible = false
        binding.networkErrorLayout.errorLayout.isVisible = false
        binding.chatRecycler.isVisible = true
        chatRecyclerAdapter.submitList(messages)
    }

    private fun provideMessageLoading() {
        binding.pbLoading.isVisible = true
        binding.emptyLayout.errorLayout.isVisible = false
        binding.networkErrorLayout.errorLayout.isVisible = false
        binding.chatRecycler.isVisible = false
    }

    private fun provideMessageEmpty() {
        binding.pbLoading.isVisible = false
        binding.emptyLayout.errorLayout.isVisible = true
        binding.networkErrorLayout.errorLayout.isVisible = false
        binding.chatRecycler.isVisible = false
    }

    private fun provideMessageError() {
        binding.pbLoading.isVisible = false
        binding.emptyLayout.errorLayout.isVisible = false
        binding.networkErrorLayout.errorLayout.isVisible = true
        binding.chatRecycler.isVisible = false
    }

    private fun provideReactionListSuccess(reactions: List<Reaction>) {
        dialogLayoutBinding.pbLoading.isVisible = false
        dialogLayoutBinding.emojiRecycler.isVisible = true
        reactionRecyclerAdapter.submitList(reactions)
    }

    private fun provideReactionListLoading() {
        dialogLayoutBinding.pbLoading.isVisible = true
        dialogLayoutBinding.emojiRecycler.isVisible = false
    }

    private fun provideReactionListEmpty() {
        dialogLayoutBinding.pbLoading.isVisible = false
        dialogLayoutBinding.emojiRecycler.isVisible = false
    }

    private fun provideReactionListError() {
        dialogLayoutBinding.pbLoading.isVisible = false
        dialogLayoutBinding.emojiRecycler.isVisible = false
    }

    private fun setChannel(channel: String) = "#$channel"

    private fun setTopic(topic: String) = "Topic: #$topic"

    private fun setupStatusBar() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.green)
    }

    private fun addListeners() {
        with(binding) {
            networkErrorLayout.networkButton.setOnClickListener { store.accept(ChatEvent.Ui.GetMessages) }
            emptyLayout.btnRefresh.setOnClickListener { store.accept(ChatEvent.Ui.GetMessages) }
            messageFieldLayout.imageBtnSend.setOnClickListener { sendMessage() }
            messageFieldLayout.etMessageField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    messageFieldLayout.imageBtnSend.setImageResource(getImageBtnResource(count))

                override fun afterTextChanged(s: Editable?) {}

            })
        }

    }

    private fun setBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireActivity())
        _dialogLayoutBinding = BottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogLayoutBinding.root)
        dialogLayoutBinding.emojiRecycler.layoutManager =
            GridLayoutManager(requireActivity(), 6)
        dialogLayoutBinding.emojiRecycler.adapter = reactionRecyclerAdapter
    }

    private fun showBottomSheetDialog(messageId: Int) {
        reactionRecyclerAdapter.onEmojiDialogClickListener =
            object : ReactionRecyclerAdapter.OnEmojiDialogClickListener {
                override fun onEmojiDialogClick(emoji: Reaction) {
//                    viewModel.addNewEmoji(message, emoji)
                    store.accept(ChatEvent.Ui.AddReaction(emoji, messageId))
                    Log.d("ChatActivity", "emoji selected $emoji")
                    bottomSheetDialog.dismiss()
                }
            }
        bottomSheetDialog.show()
    }

    private fun showErrorDialog() {
        alertDialog = AlertDialog.Builder(activity, R.style.CustomAlertDialogStyle)
            .setCancelable(true)
            .setView(
                SendingMessageErrorDialogLayoutBinding.inflate(LayoutInflater.from(this.context))
                    .apply {
                        btnRetrySendingMessage.setOnClickListener {
                            sendMessage()
                            alertDialog?.cancel()
                        }
                    }
                    .root
            )
            .create()
        alertDialog?.show()
    }

    private fun getCurrentUserId() = store.accept(ChatEvent.Ui.GetCurrentUserId)

    private fun getReactionList() = store.accept(ChatEvent.Ui.GetReactionList)

    private fun getMessageList() = store.accept(ChatEvent.Ui.GetMessages)

    private fun sendMessage() {
        store.accept(ChatEvent.Ui.SendMessage(getText()))
        Log.d("ChatActivity", "text to send ${getText()}")
    }

    private fun processAddEmoji(messageId: Int, emoji: Reaction) {
        store.accept(ChatEvent.Ui.AddReaction(emoji, messageId))
    }

    private fun processDeleteEmoji(messageId: Int, emoji: Reaction) {
        store.accept(ChatEvent.Ui.DeleteReaction(emoji, messageId))
    }

    private fun getText() = binding.messageFieldLayout.etMessageField.text.toString().trim()


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

