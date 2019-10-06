import Vue from 'vue'

//const page = Vue.resource('/admin_panel/user-messages{/id}');

export default {
    getUsers: page => Vue.http.get('/admin-panel/users', {params: {page}}),
    banOrUnban: userId => Vue.http.post(`/admin-panel/change-isBan/${userId}`),
    page: (page, userId) => Vue.http.get(`/admin-panel/user-messages/${userId}`, {params: {page}})
}