package com.dongchyeon.simplechatapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.socket.client.IO
import io.socket.client.Socket

@HiltAndroidApp
class SimpleChatApp : Application() {

    companion object {
        val socket: Socket = IO.socket(BuildConfig.BASE_URL)

        var userName: String = ""
        var roomName: String = ""
    }

    init {
        socket.connect()
    }
}