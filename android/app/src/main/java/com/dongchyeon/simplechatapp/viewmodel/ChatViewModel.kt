package com.dongchyeon.simplechatapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongchyeon.simplechatapp.SimpleChatApp.Companion.roomName
import com.dongchyeon.simplechatapp.SimpleChatApp.Companion.socket
import com.dongchyeon.simplechatapp.SimpleChatApp.Companion.userName
import com.dongchyeon.simplechatapp.data.model.ChatData
import com.dongchyeon.simplechatapp.data.model.RoomData
import com.dongchyeon.simplechatapp.data.repository.NetworkRepository
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
    private val repository: NetworkRepository,
) : ViewModel() {
    private val chatList = mutableListOf<ChatData>()
    private val _chatData = MutableLiveData<List<ChatData>>()
    val chatData: LiveData<List<ChatData>> = _chatData

    private val gson = Gson()
    private val updateChat = Emitter.Listener { args ->
        val chat = gson.fromJson(args[0].toString(), ChatData::class.java)
        addChat(chat)
    }

    init {
        _chatData.value = chatList

        socket.on("update", updateChat)
        socket.emit("enter", gson.toJson(RoomData(userName, roomName)))
    }


    private fun addChat(chat: ChatData) {
        chatList.add(chat)
        _chatData.postValue(chatList)
    }

    fun sendMessage(message: String) {
        val chat = ChatData(
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

            val result = repository.uploadImage(body).body()
            socket.emit(
                "newImage",
                gson.toJson(
                    ChatData(
                        "IMAGE",
                        userName,
                        roomName,
                        result!!.imageUri,
                        System.currentTimeMillis()
                    )
                )
            )
        }
        addChat(
            ChatData(
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
        socket.emit("left", gson.toJson(RoomData(userName, roomName)))
    }
}