package com.dongchyeon.simplechatapp.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.dongchyeon.simplechatapp.SimpleChatApp
import com.dongchyeon.simplechatapp.data.model.Chat
import kotlinx.coroutines.launch

@Composable
fun Conversation(chats: List<Chat>, modifier: Modifier = Modifier) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = scrollState
    ) {
        items(chats) { chat ->
            when (chat.type) {
                "ENTER" -> CenterMessageCard(chat, modifier)
                "LEFT" -> CenterMessageCard(chat, modifier)
                "MESSAGE" -> if (chat.from != SimpleChatApp.userName) {
                    LeftMessageCard(chat, modifier)
                } else {
                    RightMessageCard(chat, modifier)
                }
                "IMAGE" -> if (chat.from != SimpleChatApp.userName) {
                    LeftImageCard(chat, modifier)
                } else {
                    RightImageCard(chat, modifier)
                }
            }
        }

        coroutineScope.launch {
            scrollState.scrollToItem(if (chats.size - 1 < 0) 0 else chats.size - 1)
        }
    }
}