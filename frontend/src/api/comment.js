import Vue from 'vue'

const  comments = Vue.resource('/comment{/id}');

export default {
    add: comment => comments.save({}, comment), //здесь id можно не указывать т.к. при создании мы его не знаем
}