import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'

let stompClient = null
const handlers = []

export function connect() {
    const socket = new SockJS('/my-first-websocket')
    stompClient = Stomp.over(socket)
    stompClient.debug = () => {}  // Отключаем логирование stomp
    stompClient.connect({}, frame => {
        stompClient.subscribe('/topic/activity', message => {
            handlers.forEach(handler => handler(JSON.parse(message.body)))
        })
    })
}

export function addHandler(handler) {
    handlers.push(handler)
}

export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
    console.log('Disconnected')
}
export function sendMessage(message) {
    stompClient.send('/app/changeMessage', {}, JSON.stringify(message))
}