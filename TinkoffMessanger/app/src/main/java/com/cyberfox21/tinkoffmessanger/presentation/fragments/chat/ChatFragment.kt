package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.BottomSheetDialogLayoutBinding
import com.cyberfox21.tinkoffmessanger.databinding.FragmentChatBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.reactions.ReactionRecyclerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChatFragment : Fragment() {

    private lateinit var ctx: Context

    private lateinit var fragmentTopic: Topic

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentChatBinding = null")

    private lateinit var viewModel: ChatViewModel

    private val chatRecyclerAdapter = ChatRecyclerAdapter()
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
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private fun setupViews() {
        binding.tvChatTopic.text = fragmentTopic.title
        binding.chatRecycler.adapter = chatRecyclerAdapter
    }

    private fun addListeners() {
        with(binding) {
            imageBtnSend.setOnClickListener {
                sendMessage()
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
        chatRecyclerAdapter.onLongMessageClickListener =
            object : ChatRecyclerAdapter.OnLongMessageClickListener {
                override fun onLongMessageClick(message: Message) {
                    showBottomSheetDialog(message)
                }
            }
    }

    private fun setBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(ctx)
        val dialogLayoutBinding = BottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogLayoutBinding.root)
        dialogLayoutBinding.emojiRecycler.layoutManager = GridLayoutManager(ctx, 6)
        dialogLayoutBinding.emojiRecycler.adapter = reactionRecyclerAdapter
        reactionRecyclerAdapter.submitList(viewModel.reactionList)
    }

    private fun showBottomSheetDialog(message: Message) {
        reactionRecyclerAdapter.onEmojiDialogClickListener =
            object : ReactionRecyclerAdapter.OnEmojiDialogClickListener {
                override fun onEmojiDialogClick(emoji: String) {
                    viewModel.addNewEmoji(message, emoji)
                    Log.d("ChatActivity", "emoji selected $emoji")
                    bottomSheetDialog.dismiss()
                }
            }
        bottomSheetDialog.show()
    }

    private fun sendMessage() {
        val image = R.drawable.ed
        val name = resources.getString(R.string.example_name)
        val message = binding.etMessageField.text.toString()
        viewModel.sendMessage(image, name, message)
        binding.etMessageField.setText("")
    }

    private fun observeViewModel() {
        viewModel.messageList.observe(viewLifecycleOwner) {
            chatRecyclerAdapter.submitList(it)
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

    companion object {

        const val TOPIC_EXTRA = "topic_extra"

        fun newInstance(topic: Topic): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TOPIC_EXTRA, topic)
                }
            }
        }
    }
}