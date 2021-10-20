package com.cyberfox21.tinkoffmessanger.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private lateinit var viewModel: ChatViewModel

    private val chatRecyclerAdapter = ChatRecyclerAdapter()
    private val customMessageItemDecorator = CustomMessageItemDecorator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding()
        setContentView(binding.root)
        setViewModel()
        setupRecyclerView()
        addListeners()
        observeViewModel()
    }

    private fun setBinding() {
        binding = ActivityChatBinding.inflate(layoutInflater)
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private fun setupRecyclerView() {
        binding.chatRecycler.adapter = chatRecyclerAdapter
        //binding.chatRecycler.addItemDecoration(customMessageItemDecorator)
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
    }

    private fun sendMessage() {
        val image = R.drawable.ed
        val name = resources.getString(R.string.example_name)
        val message = binding.etMessageField.text.toString()
        viewModel.sendMessage(image, name, message)
        binding.etMessageField.setText("")
    }

    private fun observeViewModel() {
        viewModel.messageList.observe(this) {
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

}