package com.dongchyeon.simplechatapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongchyeon.simplechatapp.adapter.ChatAdapter
import com.dongchyeon.simplechatapp.databinding.ActivityChatBinding
import com.dongchyeon.simplechatapp.util.TakePictureFromCameraOrGallery
import com.dongchyeon.simplechatapp.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val chatViewModel: ChatViewModel by viewModels()
    private val getImageContent =
        registerForActivityResult(TakePictureFromCameraOrGallery()) { result: String? ->
            if (result != null) {
                chatViewModel.sendImage(result)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
    }

    private fun init() {
        val adapter = ChatAdapter(applicationContext)

        chatViewModel.chatData.observe(this@ChatActivity) { data ->
            data.let {
                adapter.submitList(data)
                binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.adapter = adapter

        binding.sendBtn.setOnClickListener {
            val msg = binding.contentEdit.text.toString()
            if (msg.isNotEmpty()) {
                chatViewModel.sendMessage(msg)
                binding.contentEdit.setText("")
            }
        }
        binding.imageBtn.setOnClickListener {
            getImageContent.launch(Unit)
        }
    }
}