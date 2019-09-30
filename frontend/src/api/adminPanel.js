import Vue from 'vue'


export default {
    getUsers: users => Vue.http.get('/admin_panel/users', {params: users})
}