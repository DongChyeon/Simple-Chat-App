import express from 'express'
import { createServer } from 'http'
import socket from './socket.js'
import route_loader from './route/route_loader.js';
import dotenv from 'dotenv'
dotenv.config()

const app = express()
app.use(express.static('images')) // images 폴더를 읽도록 함
app.set('port', process.env.SERVER_PORT) // 서버 포트 설정
route_loader.init(app, express.Router()) // 라우팅 설정 초기화

const server = createServer(app).listen(app.get('port'), () => {
  console.log('서버를 시작했습니다. : ' + app.get('port'))
});

socket.init(server, app)  // socket.io 설정 초기화