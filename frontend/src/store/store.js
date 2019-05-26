import Vue from 'vue'
import Vuex from 'vuex'
import messagesApi from '../api/messages.js';
import commentApi from '../api/comment.js';

Vue.use(Vuex)
export default new Vuex.Store({
    state: {
        messages,
        profile: frontendData.profile
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
            const updateIndex = state.messages.findIndex(item => item.id === message.id)  // <-- 4
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
        },
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
            const result = await messagesApi.add(message)
            const data = await result.json()
            const index = state.messages.findIndex(item => item.id === data.id)

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
            const data = result.json();

            commit('addCommentMutation', comment)
        }
    }
})