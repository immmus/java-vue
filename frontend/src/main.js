import Vue from 'vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import './api/resource'
import router from 'router/router'
import App from './pages/App.vue'
import store from 'store/store.js'
import {connect} from './util/ws'
import 'vuetify/dist/vuetify.min.css'

//проверяем что поле profile заполнено и только тогда открываем сокет
if (profile) {
    connect()
}

Vue.use(Vuetify);

new Vue({
    el: '#app',
    store,
    router,
    render: a => a(App)
});
