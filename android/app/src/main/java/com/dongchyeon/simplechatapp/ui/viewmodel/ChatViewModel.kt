package com.dongchyeon.simplechatapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongchyeon.simplechatapp.SimpleChatApp.Companion.roomName
import com.dongchyeon.simplechatapp.SimpleChatApp.Companion.socket
import com.dongchyeon.simplechatapp.SimpleChatApp.Companion.userName
import com.dongchyeon.simplechatapp.data.remote.repository.NetworkRepository
import com.dongchyeon.simplechatapp.data.socket.model.Chat
import com.dongchyeon.simplechatapp.data.socket.model.Room
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.emitter.Emitter
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {
    private var chatList = listOf<Chat>()
    private val _chat = MutableLiveData<List<Chat>>()
    val chat: LiveData<List<Chat>> = _chat

    private val gson = Gson()
    private val updateChat = Emitter.Listener { args ->
        val chat = gson.fromJson(args[0].toString(), Chat::class.java)
        addChat(chat)
    }

    init {
        _chat.value = chatList

        socket.on("update", updateChat)
        socket.emit("enter", gson.toJson(Room(userName, roomName)))
    }


    private fun addChat(chat: Chat) {
        chatList += listOf(chat)
        _chat.postValue(chatList)
    }

    fun sendMessage(message: String) {
        val chat = Chat(
            "MESSAGE",
            userName,
            roomName,
            message,
            System.currentTimeMillis()
        )
        socket.emit("newMessage", gson.toJson(chat))
        addChat(chat)
    }

    fun sendImage(imagePath: String) {
        viewModelScope.launch {
            val image = File(imagePath)
            val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", image.name, requestFile)

            val result = networkRepository.uploadImage(body)
            if (result.isSuccessful) {
                socket.emit(
                    "newImage",
                    gson.toJson(
                        Chat(
                            "IMAGE",
                            userName,
                            roomName,
                            result.body()!!.imageUri,
                            System.currentTimeMillis()
                        )
                    )
                )
            } else {
                Log.d("Error", result.toString())
            }
        }
        addChat(
            Chat(
                "IMAGE",
                userName,
                roomName,
                imagePath,
                System.currentTimeMillis()
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        socket.emit("left", gson.toJson(Room(userName, roomName)))
    }
}