import Vue from 'vue'
import MessagesList from "../pages/MessagesList.vue"
import VueRouter from 'vue-router'
import Auth from '../pages/Auth.vue'
import Profile from '../pages/Profile.vue'
import Subscriptions from "../pages/Subscriptions.vue";

Vue.use(VueRouter)

const routes = [
    {path: '/', component: MessagesList},
    {path: '/auth', component: Auth},
    {path: '/subscriptions/:id', component: Subscriptions},
    /*
    Теперь все URL вида "/user/foo" и "/user/bar" будут соответствовать одному маршруту.
     Для конктретного юзера его профиль будет отображаться по "/user", но для остальных пользователей, чтобы перейти в его профиль
     необходимо будет указать конкретный id пользователя (/user/123), поэтому необходимо добавить динамический пуст - ":id",
     но если мы не добавим "?" в конце, при попытке перейти на "/user", мы упадем в наш стандартный url под звездочнокой,
     потому что "/user" и "/user/" не одно и тоже. "?" - означает, что параметр перед ним является не обязательным.
     Дополнительную информацию о динамических путях можно почтитать:
     >> https://router.vuejs.org/ru/guide/essentials/dynamic-matching.html
    */
    {path: '/user/:id?', component: Profile},
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