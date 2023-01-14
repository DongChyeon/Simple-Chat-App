package com.dongchyeon.simplechatapp.data.socket.model

import java.text.SimpleDateFormat
import java.util.*

data class Chat(
    val type: String,   // ENTER, LEFT, MESSAGE, IMAGE
    val from: String,
    val to: String,
    val content: String,
    val sendTime: Long
) {
    fun getFormattedTime(sendTime: Long): String {
        return SimpleDateFormat("hh:mm a", Locale.KOREA).format(Date(sendTime))
    }
}

