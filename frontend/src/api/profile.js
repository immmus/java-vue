import Vue from 'vue'
// Документация к vue-resource
// https://github.com/pagekit/vue-resource/blob/develop/docs/http.md
const  profile = Vue.resource('/profile{/id}');

export default  {
    get: id => profile.get({id}),
    // вызывает post метод с беренда который находится по пути ->> ru.immmus.controller.ProfileController
    changeSubscription: channelId => Vue.http.post(`/profile/change-subscription/${channelId}`),
    subscriberList: channelId => Vue.http.get(`/profile/get-subscribers/${channelId}`),
    changeSubscriptionStatus: subscriberId => Vue.http.post(`/profile/change-status-subscriber/${subscriberId}`)
}