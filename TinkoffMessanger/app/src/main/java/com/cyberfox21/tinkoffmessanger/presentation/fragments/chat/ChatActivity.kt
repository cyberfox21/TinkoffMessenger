package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

//class ChatActivity : AppCompatActivity() {
//
//    private lateinit var bottomSheetDialog: BottomSheetDialog
//
//    private lateinit var binding: ActivityChatBinding
//
//    private lateinit var viewModel: ChatViewModel
//
//    private val chatRecyclerAdapter = ChatRecyclerAdapter()
//    private val reactionRecyclerAdapter = ReactionRecyclerAdapter()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setBinding()
//        setContentView(binding.root)
//        setViewModel()
//        setupRecyclerView()
//        addListeners()
//        observeViewModel()
//        setBottomSheetDialog()
//    }
//
//    private fun setBinding() {
//        binding = ActivityChatBinding.inflate(layoutInflater)
//    }
//
//    private fun setViewModel() {
//        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
//    }
//
//    private fun setupRecyclerView() {
//        binding.chatRecycler.adapter = chatRecyclerAdapter
//    }
//
//    private fun addListeners() {
//        with(binding) {
//            imageBtnSend.setOnClickListener {
//                sendMessage()
//            }
//            etMessageField.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
//                    imageBtnSend.setImageResource(getImageBtnResource(count))
//
//                override fun afterTextChanged(s: Editable?) {}
//
//            })
//        }
//        chatRecyclerAdapter.onLongMessageClickListener =
//            object : ChatRecyclerAdapter.OnLongMessageClickListener {
//                override fun onLongMessageClick(message: Message) {
//                    showBottomSheetDialog(message)
//                }
//            }
//    }
//
//    private fun setBottomSheetDialog() {
//        bottomSheetDialog = BottomSheetDialog(this)
//        val dialogLayoutBinding = BottomSheetDialogLayoutBinding.inflate(layoutInflater)
//        bottomSheetDialog.setContentView(dialogLayoutBinding.root)
//        dialogLayoutBinding.emojiRecycler.layoutManager = GridLayoutManager(this, 6)
//        dialogLayoutBinding.emojiRecycler.adapter = reactionRecyclerAdapter
//        reactionRecyclerAdapter.submitList(viewModel.reactionList)
//    }
//
//    private fun showBottomSheetDialog(message: Message) {
//        reactionRecyclerAdapter.onEmojiDialogClickListener =
//            object : ReactionRecyclerAdapter.OnEmojiDialogClickListener {
//                override fun onEmojiDialogClick(emoji: String) {
//                    viewModel.addNewEmoji(message, emoji)
//                    Log.d("ChatActivity", "emoji selected $emoji")
//                    bottomSheetDialog.dismiss()
//                }
//            }
//        bottomSheetDialog.show()
//    }
//
//    private fun sendMessage() {
//        val image = R.drawable.ed
//        val name = resources.getString(R.string.example_name)
//        val message = binding.etMessageField.text.toString()
//        viewModel.sendMessage(image, name, message)
//        binding.etMessageField.setText("")
//    }
//
//    private fun observeViewModel() {
//        viewModel.messageList.observe(this) {
//            chatRecyclerAdapter.submitList(it)
//        }
//    }
//
//    private fun getImageBtnResource(count: Int): Int = when (count) {
//        0 -> {
//            R.drawable.ic_attach_btn
//        }
//        else -> {
//            R.drawable.ic_send_btn
//        }
//    }
//
//}
