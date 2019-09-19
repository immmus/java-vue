import Vue from 'vue'
import Vuex from 'vuex'
import messagesApi from '../api/messages.js';
import commentApi from '../api/comment.js';

Vue.use(Vuex);
export default new Vuex.Store({
    state: {
        messages,
        profile,
        ...frontendData
    },
    getters: {
        sortedMessages: state => (state.messages || []).sort((a, b) => -(a.id - b.id))
    },
    mutations: {
        addMessageMutation(state, message) {
            state.messages = [
                ...state.messages,
                message
            ]
        },
        updateMessageMutation(state, message) {
            const updateIndex = state.messages.findIndex(item => item.id === message.id);  // <-- 4
            //допустим массив  [1, 2, 3, 4, 5]
            state.messages = [
                ...state.messages.slice(0, updateIndex), // <-- [1, 2]
                message, // <-- [3] - то есть при апдейте, мы заменяем сообщение под индексом 3 новым message
                ...state.messages.slice(updateIndex + 1) // <-- [4, 5]
            ]
        },
        removeMessageMutation(state, message) {
            const removeIndex = state.messages.findIndex(item => item.id === message.id);
            if (removeIndex > -1) {
                state.messages = [
                    ...state.messages.slice(0, removeIndex),
                    ...state.messages.slice(removeIndex + 1)
                ]
            }
        },
        addCommentMutation(state, comment) {
            // Достаем из нашего комментария message (согласно доменной модели в бекенде),
            // у  него берем id и вставляем его в state
            const updateIndex = state.messages.findIndex(item => item.id === comment.message.id);
            const message = state.messages[updateIndex];

            // проверка на то, чтобы случайно не добавить 2 одинаковых комментария
            // проверяет на то, что в нашей коллекции нет элемента с похожим id
            if (!message.comments.find(it => it.id === comment.id)) {
                state.messages = [
                    ...state.messages.slice(0, updateIndex),
                    {
                        ...message, // раскладываем наше сообшение в словаре
                        comments: [ // и указываем, что список комментов в сообщении изменяется
                            ...message.comments, // берем все наши комментарии сообщения
                            comment // и вставляем наш коммпент в конец листа
                        ]
                    },
                    ...state.messages.slice(updateIndex + 1)
                ]
            }
        },
        addMessagePageMutation(state, message) {
            // Создаем мапу targetMessages
            // Для того чтобы убрать дубликаты сообщений, которые могут появится.
            // Например когда у нас был список сообщений и на сервере произошли какие-то модификации,
            // либо кто-то добавил сообщение и оно прилетело через веб-сокеты, и мы загружая его через ленивую загрузку
            // можем получить множество дублей.
            const targetMessages = state.messages
                .concat(message)
                .reduce((result, value) => {
                    result[value.id] = value;  // таким образом, мы пробегаемся по листу и складываем в мапу, где ключ это id
                    return result                   // а значением будет само собщение и если вдруг у нас будут 2 елемента с одинаковы id они будут сложены в один елемент
                }, /*чтобы уменьшить кол-во дублей передаем на вход обьет,
                это будет аргумент для первой итерации ->> */ {});
            state.messages = Object.values(targetMessages) // в наш лист мы положим только значения из мапы
            },
        updateTotalPagesMutation(state, totalPages) {
            //сохраняем в state количество страниц
            state.totalPages = totalPages
        },
        updateCurrentPageMutation(state, currentPage) {
            //сохраняем в state текущую страницу
            state.currentPage =  currentPage
        }
    },
    actions: {
        addMessageAction: async function ({commit, state}, message) {
            /* messagesApi.add(message)
                 .then(result =>
                 result.json().then(data => {
                     const index = this.messages.findIndex(item => item.id === data.id);
                       if (index > -1) {
                         this.messages.splice(index, 1, data)
                     } else {
                         this.messages.push(data)
                     }
                 }))  Это равняется тому, что написанно ниже, только писать async уже не надо. Оставил для примера*/
            const result = await messagesApi.add(message);
            const data = await result.json();
            const index = state.messages.findIndex(item => item.id === data.id);

            if (index > -1) {
                commit('updateMessageMutation', data)
            } else {
                commit('addMessageAction', data)
            }
        },
        async updateMessageAction({commit}, message) {
            const result = await messagesApi.update(message);
            const data = await result.json();
            commit('updateMessageMutation', data)
        },
        async removeMessageAction({commit}, message) {
            // {id: message.id} передает не только id, но и объект
            const result = await messagesApi.remove(/*{id: message.id}*/ message.id);
            if (result.ok) {
                commit('removeMessageMutation', message)
            }
        },
        async addCommentAction ({commit, state}, comment) {
            const result = await commentApi.add(comment);
            const data = await result.json();

            commit('addCommentMutation', data)
        },
        async loadPageAction({commit, state}) {
            //
            const response = await messagesApi.page(state.currentPage + 1);
            const  data = await response.json();

            commit('addMessagePageMutation', data.messages);
            commit('updateTotalPagesMutation', data.totalPages);
            // берем мимнимум из этих значений, для того чтобы не накрутить currentPage больше totalPages
            // ведь мы постоянно крутим странички +1
            commit('updateCurrentPageMutation', Math.min(data.currentPage, data.totalPages - 1))
        }
    }
})