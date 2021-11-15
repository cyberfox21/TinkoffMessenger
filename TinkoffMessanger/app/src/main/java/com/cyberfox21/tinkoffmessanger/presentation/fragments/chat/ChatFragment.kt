package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.BottomSheetDialogLayoutBinding
import com.cyberfox21.tinkoffmessanger.databinding.FragmentChatBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.AlienMessageDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.DateDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.MainChatRecyclerAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.MyMessageDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.reactions.ReactionRecyclerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChatFragment : Fragment() {

    private lateinit var ctx: Context

    private lateinit var fragmentChannel: Channel
    private lateinit var fragmentTopic: Topic

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentChatBinding = null")

    private var _dialogLayoutBinding: BottomSheetDialogLayoutBinding? = null
    private val dialogLayoutBinding
        get() = _dialogLayoutBinding
            ?: throw RuntimeException("BottomSheetDialogLayoutBinding = null")


    private lateinit var viewModel: ChatViewModel

    private val chatRecyclerAdapter = MainChatRecyclerAdapter()
    private val reactionRecyclerAdapter = ReactionRecyclerAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
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
        _binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setupViews()
        addListeners()
        observeViewModel()
        setBottomSheetDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArguments() {
        val args = requireArguments()
        if (!args.containsKey(TOPIC_EXTRA)) throw RuntimeException("Param topic is absent")
        val topic = args.getParcelable<Topic>(TOPIC_EXTRA)
        topic?.let { fragmentTopic = it }
        if (!args.containsKey(CHANNEL_EXTRA)) throw RuntimeException("Param channel is absent")
        val channel = args.getParcelable<Channel>(CHANNEL_EXTRA)
        channel?.let { fragmentChannel = it }
    }

    private fun setViewModel() {
        val chatViewModelFactory = ChatViewModelFactory(fragmentChannel, fragmentTopic)
        viewModel = ViewModelProvider(this, chatViewModelFactory)[ChatViewModel::class.java]
    }

    private fun setupViews() {
        binding.tvChatTopic.text = fragmentTopic.title
        chatRecyclerAdapter.addDelegate(AlienMessageDelegateAdapter())
        chatRecyclerAdapter.addDelegate(MyMessageDelegateAdapter())
        chatRecyclerAdapter.addDelegate(DateDelegateAdapter())
        binding.chatRecycler.adapter = chatRecyclerAdapter
    }

    private fun addListeners() {
        with(binding) {
            imageBtnSend.setOnClickListener {
                viewModel.sendMessage(binding.etMessageField.text)
            }
            etMessageField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    imageBtnSend.setImageResource(getImageBtnResource(count))

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

    private fun observeViewModel() {
        viewModel.reactionsListStateLD.observe(
            viewLifecycleOwner,
            { processedReactionsListState(it) })
        viewModel.chatScreenStateLD.observe(viewLifecycleOwner, { processedChatScreenState(it) })
    }

    private fun setBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(ctx)
        _dialogLayoutBinding = BottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogLayoutBinding.root)
        dialogLayoutBinding.emojiRecycler.layoutManager = GridLayoutManager(ctx, 6)
        dialogLayoutBinding.emojiRecycler.adapter = reactionRecyclerAdapter
    }

    private fun showBottomSheetDialog(message: Message) {
        reactionRecyclerAdapter.onEmojiDialogClickListener =
            object : ReactionRecyclerAdapter.OnEmojiDialogClickListener {
                override fun onEmojiDialogClick(emoji: Reaction) {
//                    viewModel.addNewEmoji(message, emoji)
//                    Log.d("ChatActivity", "emoji selected $emoji")
                    bottomSheetDialog.dismiss()
                }
            }
        bottomSheetDialog.show()
    }

    private fun processedReactionsListState(it: ReactionsListState) {
        when (it) {
            is ReactionsListState.Result -> {
                dialogLayoutBinding.pbLoading.isVisible = false
                reactionRecyclerAdapter.submitList(it.items)
            }
            ReactionsListState.Loading -> dialogLayoutBinding.pbLoading.isVisible = true
            is ReactionsListState.Error -> {
                Log.d("ReactionsListState", "${it.error.message}")
                dialogLayoutBinding.pbLoading.isVisible = false
                Toast.makeText(
                    this.context,
                    "ReactionsListState ${it.error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun processedChatScreenState(it: ChatScreenState) {
        when (it) {
            is ChatScreenState.Result -> {
                binding.pbLoading.isVisible = false
                chatRecyclerAdapter.submitList(it.items)
            }
            ChatScreenState.Loading -> binding.pbLoading.isVisible = true
            is ChatScreenState.Error -> {
                binding.pbLoading.isVisible = false
                Log.d("ChatScreenState", "${it.error.message}")
                Toast.makeText(
                    this.context,
                    "ChatScreenState ${it.error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getImageBtnResource(count: Int): Int = when (count) {
        0 -> {
            R.drawable.ic_attach_btn
        }
        else -> {
            R.drawable.ic_send_btn
        }
    }

//    private fun configureToolbar() {
//        binding.toolbarLayout.toolbar.setNavigationIcon(R.drawable.ic_back)
//        binding.toolbarLayout.toolbar.setNavigationOnClickListener {
//            activity?.onBackPressed()
//        }
//    }

    companion object {

        const val CHANNEL_EXTRA = "channel_extra"
        const val TOPIC_EXTRA = "topic_extra"

        fun newInstance(channel: Channel, topic: Topic): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHANNEL_EXTRA, channel)
                    putParcelable(TOPIC_EXTRA, topic)
                }
            }
        }
    }
}
