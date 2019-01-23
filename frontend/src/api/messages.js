import Vue from 'vue'

const  messages = Vue.resource('/message{/id}');

export default  {
    add: message => messages.save({}, message), //здесь id можно не указывать т.к. при создании мы его не знаем
    update: message => messages.update({id: message.id}, message),
    remove: id => messages.remove({id: id} )
}