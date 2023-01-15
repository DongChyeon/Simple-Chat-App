package com.dongchyeon.simplechatapp.data.socket.datasource

import io.socket.client.Socket
import io.socket.emitter.Emitter

class AppSocket(private val socket: Socket) {

    val isConnected: Boolean get() = socket.connected()

    fun connect() {
        socket.open()
    }

    fun disconnect() {
        socket.close()
    }

    fun on(event: String, listener: Emitter.Listener) {
        socket.on(event, listener)
    }
}