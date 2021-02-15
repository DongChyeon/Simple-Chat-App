const express = require('express')
const http = require('http')
const app = express()
const server = http.createServer(app)

const io = require('socket.io')(server)

io.sockets.on('connection', (socket) => {
  console.log(`Socket connected : ${socket.id}`)

  socket.on('subscribe', (data) => {
    const roomData = JSON.parse(data)
    const username = roomData.username
    const roomNumber = roomData.roomNumber

    socket.join(`${roomNumber}`)
    console.log(`[Username : ${username}] entered [room number : ${roomNumber}]`)
    
    const subData = {
      type : "ENTER",
      content : `${username} entered the room`  
    }
    socket.broadcast.to(`${roomNumber}`).emit('update', JSON.stringify(subData))
  })

  socket.on('unsubscribe', (data) => {
    const roomData = JSON.parse(data)
    const username = roomData.username
    const roomNumber = roomData.roomNumber

    socket.leave(`${roomNumber}`)
    console.log(`[Username : ${username}] left [room number : ${roomNumber}]`)

    const unsubData = {
      type : "LEFT",
      content : `${username} left the room`  
    }
    socket.broadcast.to(`${roomNumber}`).emit('update', JSON.stringify(unsubData))
  })

  socket.on('newMessage', (data) => {
    const messageData = JSON.parse(data)
    console.log(`[Room Number ${messageData.to}] ${messageData.from} : ${messageData.content}`)
    io.to(`${messageData.to}`).emit('update', JSON.stringify(messageData))
  })

  socket.on('disconnect', () => {
    console.log(`Socket disconnected : ${socket.id}`)
  })
})

server.listen(80, () => {
  console.log(`Server listening at http://localhost:80`)
})