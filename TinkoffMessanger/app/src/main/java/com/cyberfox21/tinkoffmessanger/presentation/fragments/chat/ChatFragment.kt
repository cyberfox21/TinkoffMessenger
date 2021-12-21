package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.BottomSheetDialogLayoutBinding
import com.cyberfox21.tinkoffmessanger.databinding.FragmentChatBinding
import com.cyberfox21.tinkoffmessanger.databinding.SendingMessageErrorDialogLayoutBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.common.MainActivity
import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import com.cyberfox21.tinkoffmessanger.presentation.common.copyToClipboard
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.ChatItemDecoration
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.clicklisteners.OnLongMessageClickListener
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.clicklisteners.OnReactionClickListener
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.ChatDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.MessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.reactions.ReactionListAdapter
import com.cyberfox21.tinkoffmessanger.presentation.util.KeyboardHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject


class ChatFragment : ElmFragment<ChatEvent, ChatEffect, ChatState>() {

    @Inject
    internal lateinit var actor: ChatActor

    override val initEvent: ChatEvent = ChatEvent.Ui.GetCurrentUserId

    private var refreshStatus: RefreshStatus = RefreshStatus.RELOAD_USER

    private lateinit var fragmentChannelName: String
    private lateinit var fragmentTopicName: String

    internal lateinit var bottomSheetDialog: BottomSheetDialog

    private var alertDialog: AlertDialog? = null

    private var _binding: FragmentChatBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException(getString(R.string.null_chat_fragment_binding))

    private var _bottomSheetBinding: BottomSheetDialogLayoutBinding? = null
    private val dialogLayoutBinding
        get() = _bottomSheetBinding
            ?: throw RuntimeException(getString(R.string.null_bottom_sheet_dialog))

    private val chatAdapter =
        PaginationDelegateAdapter(PaginationHelperAdapter { position -> loadNextMessages(position) })
    private val emojiListAdapter = ReactionListAdapter()

    private val onLongMsgClickListener = object : OnLongMessageClickListener {
        override fun onLongMessageClick(message: MessageDelegateItem, mode: BottomDialogMode) {
            showBottomSheetDialog(message, mode)
        }
    }

    private val onEmojiClickListener = object : OnReactionClickListener {
        override fun onReactionClick(clickMode: ClickEmojiMode, messageId: Int, emoji: Reaction) =
            when (clickMode) {
                ClickEmojiMode.ADD_EMOJI -> processAddEmoji(messageId, emoji)
                ClickEmojiMode.DELETE_EMOJI -> processDeleteEmoji(messageId, emoji)
            }
    }

    private val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            processScroll()
        }
    }


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
        _bottomSheetBinding = null
    }


    override fun createStore(): Store<ChatEvent, ChatEffect, ChatState> =
        ChatStoreFactory(actor).provide()

    override fun render(state: ChatState) {
        when (state.messageStatus) {
            ResourceStatus.SUCCESS -> provideMessageSuccess(state.chatItems)
            ResourceStatus.LOADING -> provideMessageLoading(state.updateType)
            ResourceStatus.EMPTY -> provideMessageEmpty()
            ResourceStatus.ERROR -> provideMessageError()
        }

        when (state.reactionsListStatus) {
            ResourceStatus.SUCCESS -> provideReactionListSuccess(state.reactions)
            ResourceStatus.LOADING, ResourceStatus.EMPTY, ResourceStatus.ERROR -> {
            }
        }
    }

    override fun handleEffect(effect: ChatEffect) {
        when (effect) {
            ChatEffect.PrepareReactionList -> {
                refreshStatus = RefreshStatus.RELOAD_REACTIONS
                getReactionList()
            }

            ChatEffect.StartChatFragmentWork -> {
                refreshStatus = RefreshStatus.RELOAD_MESSAGES
                getMessageList()
            }

            ChatEffect.MessageSendingSuccess -> clearInput()

            ChatEffect.MessageSendingError -> showErrorDialog(
                getErrorMessage(R.string.sending_message_dialog_error)
            ) { sendMessage() }

            ChatEffect.MessageEditingSuccess -> clearInput()

            is ChatEffect.MessageEditingError -> showErrorDialog(
                getErrorMessage(R.string.editing_message_dialog_error)
            ) { processEditMessage(effect.msgId, effect.text) }

            is ChatEffect.ChangeTopicError -> showErrorDialog(
                getErrorMessage(R.string.change_topic_dialog_error)
            ) { processEditTopic(effect.msgId, effect.topic) }

            is ChatEffect.MessageDeletingError -> showErrorDialog(
                getErrorMessage(R.string.deleting_message_dialog_error)
            ) { processDeleteMessage(effect.messageId) }

            ChatEffect.EmojiAddedError -> showErrorDialog(
                getErrorMessage(R.string.add_emoji_dialog_error)
            ) { reAddEmoji() }

            ChatEffect.EmojiDeletedError -> showErrorDialog(
                getErrorMessage(R.string.delete_emoji_dialog_error)
            ) { reDeleteEmoji() }

            ChatEffect.ShowNetworkError -> provideError()

            ChatEffect.ScrollToBottom -> scrollToBottom()
            is ChatEffect.ScrollToPosition -> scrollToPosition(effect.position)
        }
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
            ContextCompat.getColor(requireContext(), R.color.green)
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
        with(chatAdapter) {
            addDelegate(LoadingDelegateAdapter())
            addDelegate(AlienMessageDelegateAdapter(onEmojiClickListener, onLongMsgClickListener))
            addDelegate(MyMessageDelegateAdapter(onEmojiClickListener, onLongMsgClickListener))
            addDelegate(DateDelegateAdapter())
            registerAdapterDataObserver(dataObserver)
        }
        with(binding.chatRecycler) {
            addItemDecoration(
                ChatItemDecoration(resources.getDimensionPixelOffset(R.dimen.messageSpaceSize))
            )
            setHasFixedSize(true)
            adapter = chatAdapter
        }
    }

    private fun provideMessageSuccess(messages: List<ChatDelegateItem>) {
        binding.pbLoading.isVisible = false
        binding.emptyLayout.errorLayout.isVisible = false
        binding.errorLayout.errorRoot.isVisible = false
        binding.chatRecycler.isVisible = true
        chatAdapter.submitList(messages)
    }

    private fun provideMessageLoading(updateType: UpdateType) {
        when (updateType) {
            UpdateType.INITIAL -> {
                binding.pbLoading.isVisible = true
                binding.chatRecycler.isVisible = false
            }
            UpdateType.RELOAD, UpdateType.PAGINATION, UpdateType.UPDATE -> {
                binding.pbLoading.isVisible = false
                binding.chatRecycler.isVisible = true
            }

        }
        binding.emptyLayout.errorLayout.isVisible = false
        binding.errorLayout.errorRoot.isVisible = false

    }

    private fun provideMessageEmpty() {
        binding.pbLoading.isVisible = false
        binding.emptyLayout.errorLayout.isVisible = true
        binding.errorLayout.errorRoot.isVisible = false
        binding.chatRecycler.isVisible = false
    }

    private fun provideMessageError() {
        binding.pbLoading.isVisible = false
        binding.emptyLayout.errorLayout.isVisible = false
        binding.errorLayout.errorRoot.isVisible = true
        binding.chatRecycler.isVisible = false
    }

    private fun provideReactionListSuccess(reactions: List<Reaction>) {
        dialogLayoutBinding.pbLoading.isVisible = false
        dialogLayoutBinding.emojiRecycler.isVisible = false
        emojiListAdapter.submitList(reactions)
    }

    private fun provideError() {
        binding.pbLoading.isVisible = false
        binding.emptyLayout.errorLayout.isVisible = false
        binding.errorLayout.errorRoot.isVisible = true
        binding.chatRecycler.isVisible = false
    }

    private fun setChannel(channel: String) = "#$channel"

    private fun setTopic(topic: String) = "Topic: #$topic"

    private fun setupStatusBar() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.green)
    }

    private fun addListeners() {
        with(binding) {
            errorLayout.btnNetwork.setOnClickListener {
                when (refreshStatus) {
                    RefreshStatus.RELOAD_USER -> getCurrentUserId()
                    RefreshStatus.RELOAD_REACTIONS -> getReactionList()
                    RefreshStatus.RELOAD_MESSAGES -> getMessageList()
                }
            }
            emptyLayout.btnRefresh.setOnClickListener {
                when (refreshStatus) {
                    RefreshStatus.RELOAD_USER -> getCurrentUserId()
                    RefreshStatus.RELOAD_REACTIONS -> getReactionList()
                    RefreshStatus.RELOAD_MESSAGES -> getMessageList()
                }
            }
            messageFieldLayout.imageBtnSend.setOnClickListener {
                when (store.currentState.btnSendMode) {
                    ButtonSendMode.ADD -> sendMessage()
                    ButtonSendMode.EDIT -> editMessage()
                }
                KeyboardHelper.hideKeyboard(activity)
            }
            messageFieldLayout.etMessageField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, strt: Int, size: Int, aftr: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    messageFieldLayout.imageBtnSend.setImageResource(getBtnDrawable(s?.isEmpty()))

                override fun afterTextChanged(s: Editable?) {}

            })
        }
    }

    private fun setBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireActivity())
        _bottomSheetBinding = BottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogLayoutBinding.root)
        dialogLayoutBinding.emojiRecycler.layoutManager =
            GridLayoutManager(requireActivity(), EMOJI_LIST_SPAN_COUNT)
        dialogLayoutBinding.emojiRecycler.adapter = emojiListAdapter
        dialogLayoutBinding.field.imageBtnSend.setImageResource(getBtnDrawable(false))
    }

    private fun showBottomSheetDialog(message: MessageDelegateItem, mode: BottomDialogMode) {
        setBottomSheetDialog()
        when (mode) {
            BottomDialogMode.OPTIONS -> showOptionsList(message.senderId)
            BottomDialogMode.REACTION_LIST -> showListEmoji()
            BottomDialogMode.INPUT -> showInputField()
        }
        dialogLayoutBinding.addReaction.setOnClickListener {
            bottomSheetDialog.setOnDismissListener {
                showBottomSheetDialog(message, BottomDialogMode.REACTION_LIST)
            }
            bottomSheetDialog.dismiss()
        }
        dialogLayoutBinding.deleteMessage.setOnClickListener {
            processDeleteMessage(message.id)
            bottomSheetDialog.dismiss()
        }
        dialogLayoutBinding.editMessage.setOnClickListener {
            processEditMessage(message.id, message.text.toString())
            bottomSheetDialog.dismiss()
        }
        dialogLayoutBinding.changeTopic.setOnClickListener {
            bottomSheetDialog.setOnDismissListener {
                showBottomSheetDialog(message, BottomDialogMode.INPUT)
            }
            bottomSheetDialog.dismiss()
        }
        dialogLayoutBinding.copyMessage.setOnClickListener {
            processCopyMessage(message.text.toString())
            bottomSheetDialog.dismiss()
        }
        dialogLayoutBinding.field.imageBtnSend.setOnClickListener {
            bottomSheetDialog.setOnDismissListener {
                processEditTopic(
                    message.id,
                    dialogLayoutBinding.field.etMessageField.text.toString()
                )
            }
            bottomSheetDialog.dismiss()
        }
        emojiListAdapter.onEmojiListListener = object : ReactionListAdapter.OnEmojiListListener {
            override fun onEmojiClick(emoji: Reaction) {
                processAddEmoji(message.id, emoji)
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()
    }

    private fun showListEmoji() {
        dialogLayoutBinding.optionsLayout.isVisible = false
        dialogLayoutBinding.inputLayout.isVisible = false
        dialogLayoutBinding.emojiRecycler.isVisible = true
    }

    private fun showOptionsList(senderId: Int) {
        dialogLayoutBinding.optionsLayout.isVisible = true
        dialogLayoutBinding.inputLayout.isVisible = false
        dialogLayoutBinding.emojiRecycler.isVisible = false
        val value = senderId == store.currentState.currentUserId
        dialogLayoutBinding.deleteMessage.isVisible = value
        dialogLayoutBinding.editMessage.isVisible = value
        dialogLayoutBinding.changeTopic.isVisible = value

    }

    private fun showInputField() {
        dialogLayoutBinding.optionsLayout.isVisible = false
        dialogLayoutBinding.inputLayout.isVisible = true
        dialogLayoutBinding.emojiRecycler.isVisible = false
    }

    private fun processDeleteMessage(messageId: Int) {
        store.accept(ChatEvent.Ui.DeleteMessage(messageId))
    }

    private fun processEditMessage(messageId: Int, messageText: String) {
        store.currentState.selectMsgId = messageId
        store.currentState.btnSendMode = ButtonSendMode.EDIT
        binding.messageFieldLayout.etMessageField.setText(messageText)
        KeyboardHelper.showKeyboard(binding.messageFieldLayout.etMessageField, activity)
    }

    private fun processEditTopic(messageId: Int, topic: String) {
        store.currentState.selectMsgId = messageId
        store.accept(ChatEvent.Ui.ChangeMessageTopic(messageId, topic))
    }

    private fun processCopyMessage(msgText: String) {
        activity?.copyToClipboard(msgText)
    }

    private fun showErrorDialog(errorMessage: String, action: () -> Unit) {
        alertDialog = AlertDialog.Builder(activity, R.style.CustomAlertDialogStyle)
            .setOnCancelListener {
                clearInput()
                store.currentState.selectMsgId = null
                store.currentState.btnSendMode = ButtonSendMode.ADD
            }
            .setCancelable(true)
            .setView(
                SendingMessageErrorDialogLayoutBinding.inflate(LayoutInflater.from(this.context))
                    .apply {
                        networkText.text = errorMessage
                        btnRetrySendingMessage.setOnClickListener {
                            action()
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

    private fun getMessageList() =
        store.accept(ChatEvent.Ui.GetMessages(store.currentState.updateType))

    private fun loadNextMessages(pos: Int) = store.accept(ChatEvent.Ui.LoadNextMessages(pos))

    private fun sendMessage() = store.accept(ChatEvent.Ui.SendMessage(getText()))

    private fun editMessage() =
        store.accept(ChatEvent.Ui.EditMessage(getText()))

    private fun reAddEmoji() {
        if (checkSelectedArs()) store.currentState.apply {
            store.accept(ChatEvent.Ui.AddReaction(selectEmoji!!, selectMsgIdForEmoji!!))
        }
    }

    private fun processAddEmoji(messageId: Int, emoji: Reaction) {
        store.currentState.selectEmoji = emoji
        store.currentState.selectMsgIdForEmoji = messageId
        store.accept(ChatEvent.Ui.AddReaction(emoji, messageId))
    }

    private fun reDeleteEmoji() {
        if (checkSelectedArs()) store.currentState.apply {
            store.accept(ChatEvent.Ui.DeleteReaction(selectEmoji!!, selectMsgIdForEmoji!!))
        }
    }

    private fun processDeleteEmoji(messageId: Int, emoji: Reaction) {
        store.currentState.selectEmoji = emoji
        store.currentState.selectMsgIdForEmoji = messageId
        store.accept(ChatEvent.Ui.DeleteReaction(emoji, messageId))
    }

    private fun processScroll() = store.accept(ChatEvent.Ui.OnDataInserted)

    private fun scrollToBottom() = binding.chatRecycler.scrollToPosition(START_SCROLL_POSITION)

    private fun scrollToPosition(position: Int) = binding.chatRecycler.scrollToPosition(position)

    private fun getText() = binding.messageFieldLayout.etMessageField.text.toString().trim()

    private fun clearInput() {
        binding.messageFieldLayout.etMessageField.setText(CLEAR_TEXT)
        KeyboardHelper.hideKeyboard(activity)
    }

    private fun getErrorMessage(id: Int) = resources.getString(id)

    private fun getBtnDrawable(isEmpty: Boolean?) =
        if (isEmpty == true) R.drawable.ic_attach else R.drawable.ic_send

    private fun checkSelectedArs(): Boolean {
        val selectedEmoji = store.currentState.selectEmoji
        val msgId = store.currentState.selectMsgId
        return selectedEmoji != null && msgId != null
    }

    companion object {

        const val CHANNEL_EXTRA = "channel_extra"
        const val TOPIC_EXTRA = "topic_extra"

        const val START_SCROLL_POSITION = 0

        const val EMOJI_LIST_SPAN_COUNT = 6

        const val CLEAR_TEXT = ""

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
