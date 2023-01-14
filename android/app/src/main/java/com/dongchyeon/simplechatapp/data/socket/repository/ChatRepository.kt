package com.dongchyeon.simplechatapp.data.socket.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dongchyeon.simplechatapp.data.socket.datasource.AppSocket
import com.dongchyeon.simplechatapp.data.socket.model.Chat
import com.dongchyeon.simplechatapp.presentation.SimpleChatApp
import com.google.gson.Gson
import io.socket.emitter.Emitter

class ChatRepository(private val appSocket: AppSocket) {
    private val chatList = mutableListOf<Chat>()
    private val _chat = MutableLiveData<List<Chat>>()
    val chat: LiveData<List<Chat>> = _chat

    private val gson = Gson()
    private val updateChat = Emitter.Listener { args ->
        val chat = gson.fromJson(args[0].toString(), Chat::class.java)
    }

    init {
        _chat.value = chatList

        appSocket.on("update", updateChat)
    }

    fun sendMessage(message: String) {
        val chat = Chat(
            "MESSAGE",
            SimpleChatApp.userName,
            SimpleChatApp.roomName,
            message,
            System.currentTimeMillis()
        )
        SimpleChatApp.socket.emit("newMessage", gson.toJson(chat))
    }
}