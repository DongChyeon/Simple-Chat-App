package com.dongchyeon.simplechatapp.data.model

data class Chat(
    val type: String,   // ENTER, LEFT, MESSAGE, IMAGE
    val from: String,
    val to: String,
    val content: String,
    val sendTime: Long
)
