import Vue from 'vue'
import MessagesList from "../pages/MessagesList.vue"
import VueRouter from 'vue-router'
import Auth from '../pages/Auth.vue'
import Profile from '../pages/Profile.vue'

Vue.use(VueRouter)

const routes = [
    {path: '/', component: MessagesList},
    {path: '/auth', component: Auth},
    {path: '/profile', component: Profile},
    // звездочка * или wild card предназначена, чтобы перенаправлять
    // все неизветные пути или ошибки (например 404) на наш MessagesList, или любой путь который мы зададим
    // обьявлять нужно в самом конце, чтобы сначало проверить на соответсвие известным путям и если нет
    // перенаправить на "стандартную" страничку
    {path: '*', component: MessagesList}
];
export default new VueRouter({
    mode: 'history',  //чтобы убрать # в пути сайта. Подробнее -> https://router.vuejs.org/guide/essentials/history-mode.html
    routes // сокращённая запись для `routes: routes`
})