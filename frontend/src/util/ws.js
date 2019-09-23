import SockJS from 'sockjs-client'
import {Client} from '@stomp/stompjs'

let client = null
const handlers = []

export function connect() {
    const socket = new SockJS('/my-first-websocket')
    client = new Client()
    client.webSocketFactory = () => { return socket }
    client.onConnect = (frame) => frameHandler(frame)
    client.onWebsocketClose = () => onSocketClose()
    client.debug = () => {}  // Отключаем логирование stomp
    client.activate()
}

export function addHandler(handler) {
    handlers.push(handler)
}

export function frameHandler(frame) {
    console.log('Connected: ' + frame);
    client.subscribe('/topic/activity', message => {
        handlers.forEach(handler => handler(JSON.parse(message.body)));
    });
}

export function onSocketClose() {
    if (client !== null) {
        client.deactivate()
    }
    console.log('Disconnected')
}

export function sendMessage(message) {
    client.publish({destination: '/app/changeMessage', body: JSON.stringify(message)})
}