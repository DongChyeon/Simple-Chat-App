package com.dongchyeon.simplechatapp.presentation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongchyeon.simplechatapp.data.socket.model.Chat
import com.dongchyeon.simplechatapp.databinding.ActivityChatBinding
import com.dongchyeon.simplechatapp.presentation.adapter.ChatAdapter
import com.dongchyeon.simplechatapp.presentation.util.TakePictureFromCameraOrGallery
import com.dongchyeon.simplechatapp.presentation.viewmodel.ChatViewModel
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

    @Composable
    fun Conversation(chats: List<Chat>) {
        LazyColumn {
            items(chats) { chat ->
                MessageCard(chat)
            }
        }
    }

    @Composable
    fun MessageCard(msg: Chat) {
        Column(modifier = Modifier.padding(all = 12.dp)) {
            Text(
                text = msg.from,
                color = MaterialTheme.colors.secondaryVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp
            ) {
                Text(
                    text = msg.content,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }

    @Preview
    @Composable
    fun PreviewMessageCard() {
        Surface {
            MessageCard(
                msg = Chat("MESSAGE", "사용자", "2", "메시지", 0)
            )
        }
    }

    private fun init() {
        val adapter = ChatAdapter(applicationContext)

        chatViewModel.chat.observe(this@ChatActivity) { data ->
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