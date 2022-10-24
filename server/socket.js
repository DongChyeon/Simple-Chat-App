import { Server } from 'socket.io'

const socket = {}

socket.init = (server, app) => {
    const io = new Server(server)

    // 소켓 연결 코드
    io.sockets.on('connection', (socket) => {
        console.log(`Socket connected : ${socket.id}`)

        socket.on('enter', (data) => {
            const roomData = JSON.parse(data)
            const userName = roomData.userName
            const roomName = roomData.roomName

            socket.join(`${roomName}`)
            console.log(`[user name : ${userName}] entered [room name : ${roomName}]`)

            const enterData = {
                type: "ENTER",
                from: userName,
                to: roomName,
                content: `${userName} (님)이 방에 들어왔습니다.`,
                sendTime: 1
            }
            socket.broadcast.to(`${roomName}`).emit('update', JSON.stringify(enterData))
        })

        socket.on('left', (data) => {
            const roomData = JSON.parse(data)
            const userName = roomData.userName
            const roomName = roomData.roomName

            socket.leave(`${roomName}`)
            console.log(`[user name : ${userName}] left [room name : ${roomName}]`)

            const leftData = {
                type: "LEFT",
                from: userName,
                to: roomName,
                content: `${userName} (님)이 방에서 나갔습니다.`,
                sendTime: 1
            }
            socket.broadcast.to(`${roomName}`).emit('update', JSON.stringify(leftData))
        })

        socket.on('newMessage', (data) => {
            const messageData = JSON.parse(data)
            console.log(`[Room Name ${messageData.to}] ${messageData.from} : ${messageData.content}`)
            socket.broadcast.to(`${messageData.to}`).emit('update', JSON.stringify(messageData))
        })

        socket.on('newImage', (data) => {
            const messageData = JSON.parse(data)
            // 안드로이드 에뮬레이터 기준으로 url은 10.0.2.2:포트번호/, 스마트폰에서 실험해보고 싶으면 자신의 ip 주소:포트번호/ 로 해야 한다.
            messageData.content = process.env.IP_ADDRESS + messageData.content
            console.log(`[Room Name ${messageData.to}] ${messageData.from} : ${messageData.content}`)
            socket.broadcast.to(`${messageData.to}`).emit('update', JSON.stringify(messageData))
        })

        socket.on('disconnect', () => {
            console.log(`Socket disconnected : ${socket.id}`)
        })
    })
}

export default socket