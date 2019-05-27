import Vue from 'vue'
// Документация к vue-resource
// https://github.com/pagekit/vue-resource/blob/develop/docs/http.md
const  messages = Vue.resource('/message{/id}');

export default  {
    add: message => messages.save({}, message), //здесь id можно не указывать т.к. при создании мы его не знаем
    update: message => messages.update({id: message.id}, message),
    remove: id => messages.remove({id: id} ),
    // Не используем Vue.resource, потому что это будет кастомный метод с нестандартным аргументом
    // это так же можно найти в документации
    page: page => Vue.http.get('/message', {params: { page }})
}