const express = require('express')
const http = require('http')
const app = express()
app.use(express.static('images')) // images 폴더를 읽도록 함
const server = http.createServer(app)
const io = require('socket.io')(server)

const multer = require('multer')
const randomstring = require('randomstring')
const imageUpload = multer({
  storage: multer.diskStorage({
    destination: (req, file, cb) => {
      cb(null, `${__dirname}/images`) // images 폴더에 저장
    },
		filename: (req, file, cb) => {
			var fileName = randomstring.generate(25); // 랜덤 25자의 파일 이름
			var mimetype;
			switch (file.mimetype) {
				case 'image/jpeg':
					mimeType = 'jpg';
					break;
				case 'image/png':
					mimeType = 'png';
					break;
				case 'image/gif':
					mimeType = 'gif';
					break;
				case 'image/bmp':
					mimeType = 'bmp';
					break;
				default:
					mimeType = 'jpg';
					break;
			}
			cb(null, fileName + '.' + mimeType);
		},
  }),
  limits: {
    fileSize: 5 * 1024 * 1024,  // 5MB 로 크기 제한
  },
})

// 이미지 업로드
app.post('/upload', imageUpload.single('image'), (req, res) => {
  console.log(req.file)

  const imageData = {
    result : 1,
    imageUri : res.req.file.filename
  }
  res.send(JSON.stringify(imageData))
})

// 소켓 연결 코드
io.sockets.on('connection', (socket) => {
  console.log(`Socket connected : ${socket.id}`)

  socket.on('enter', (data) => {
    const roomData = JSON.parse(data)
    const username = roomData.username
    const roomNumber = roomData.roomNumber

    socket.join(`${roomNumber}`)
    console.log(`[Username : ${username}] entered [room number : ${roomNumber}]`)
    
    const enterData = {
      type : "ENTER",
      content : `${username} entered the room`  
    }
    socket.broadcast.to(`${roomNumber}`).emit('update', JSON.stringify(enterData))
  })

  socket.on('left', (data) => {
    const roomData = JSON.parse(data)
    const username = roomData.username
    const roomNumber = roomData.roomNumber

    socket.leave(`${roomNumber}`)
    console.log(`[Username : ${username}] left [room number : ${roomNumber}]`)

    const leftData = {
      type : "LEFT",
      content : `${username} left the room`  
    }
    socket.broadcast.to(`${roomNumber}`).emit('update', JSON.stringify(leftData))
  })

  socket.on('newMessage', (data) => {
    const messageData = JSON.parse(data)
    console.log(`[Room Number ${messageData.to}] ${messageData.from} : ${messageData.content}`)
    socket.broadcast.to(`${messageData.to}`).emit('update', JSON.stringify(messageData))
  })

  socket.on('newImage', (data) => {
    const messageData = JSON.parse(data)
    // 안드로이드 에뮬레이터 기준으로 url은 10.0.2.2, 스마트폰에서 실험해보고 싶으면 자신의 ip 주소로 해야 한다.
    messageData.content = 'http://10.0.2.2:80/' + messageData.content
    console.log(`[Room Number ${messageData.to}] ${messageData.from} : ${messageData.content}`)
    socket.broadcast.to(`${messageData.to}`).emit('update', JSON.stringify(messageData))
  })

  socket.on('disconnect', () => {
    console.log(`Socket disconnected : ${socket.id}`)
  })
})

server.listen(80, () => {
  console.log(`Server listening at http://localhost:80`)
})
