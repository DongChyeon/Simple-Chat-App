package com.dongchyeon.simplechatapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SimpleChatApp : Application() {

    companion object {
        var userName: String = ""
        var roomName: String = ""
    }
}