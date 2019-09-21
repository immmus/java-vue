<template>
    <v-container>
        <v-layout justify-space-around>
            <v-flex :xs6="!$vuetify.breakpoint.xsOnly">
                <div class="title mb-3">User profile</div>
                <v-layout row justify-space-between>
                    <v-flex class="px-1">
                        <v-img :src="profile.userPicture"></v-img>
                    </v-flex>
                    <v-flex>
                        <v-layout column class="px-1">
                            <v-flex>{{profile.name}}</v-flex>
                            <!-- <v-flex>{{profile.email}}</v-flex>-->
                            <v-flex>Last visit: {{profile.lastVisit}}</v-flex>
                            <v-flex>{{profile.gender}}</v-flex>
                            <!--В данном случае левая часть проверяет, если у нас есть подписчики или подписки,
                            то показываем то что содержится в правой части, в данном случае из количество-->
                            <v-flex>{{profile.subscriptions && profile.subscriptions.length}} subscriptions</v-flex>
                            <v-flex>{{profile.subscribers && profile.subscribers.length}} subscribers</v-flex>
                            <!-- <v-flex>{{profile.locale}}</v-flex>-->
                        </v-layout>
                        <!--Показываем кнопку только если это не мой профиль (isMyProfile = false)
                            При нажатии вызываем метод, который либо подписывает нас, либо отписывает-->
                        <v-btn
                                v-if="!isMyProfile"
                                @click="changeSubscription"
                        >
                            <!--Тут мы проверяем подписан ли я на данного пользователя или нет,
                             если да то выводим Unsubscribe, если нет, то Subscribe-->
                            {{ isISubscribed ? 'Unsubscribe' : 'Subscribe'}}
                        </v-btn>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import profileApi from 'api/profile'

    export default {
        name: "Profile",
        data() {
            // возвращаем объект с профилем
            return {
                profile: {}
            }
        },
        computed: {
            isMyProfile() {
                // Для проверки, что это на профиль проверям что :
                // в урле нет параметра id
                // или что id в урле эквивалентен тому, что записан у нас в сторе
                return !this.$route.params.id ||
                    this.$route.params.id === this.$store.state.profile.id
            },
            isISubscribed() {
                //Для проверки подписан ли пользователь  на текущего пользователя проверяем что:
                // у данного пользователя воо имеются подпизки или нет
                // если да, то смотрим есть ли текущий пользователь в его подписчиках
                return this.profile.subscribers &&
                    this.profile.subscribers.find(subscription => {
                        return subscription.subscriber === this.$store.state.profile.id
                    })
            }
        },
        watch: {
            // Прослушивая метод "$route" в методе updateProfile,
            // так же будет происходить обновления профиля, если мы в пределах одной странички будем
            // переходить из одного профиля в другой. То есть меняя только url.
            '$route'() {
                this.updateProfile();
            }
        },
        methods: {
            // т.к. работаем с api добавим async в методах
            // c помощью метода ".json()" мы добавляем в профайл нащи изменения
            async changeSubscription() {
                const data = await profileApi.changeSubscription(this.profile.id)
                this.profile = await data.json()
            },
            async updateProfile() {
                // если в url нет id, то берем из профиля
                // получение параметров опысывается в документации https://router.vuejs.org/ru/guide/essentials/dynamic-matching.html
                const id = this.$route.params.id || this.$store.state.profile.id
                const data = await profileApi.get(id)
                this.profile = await data.json()

                // часто этот метод юзать не рекомендуется, но в данном случае нам это нужно
                this.$forceUpdate()
            }
        },
        beforeMount() {
            // т.к. профиль у нас просто так не обновится этот метот надо дернут
            // до того как наш объект примонтируется к дереву объектов DOM в браузере
            // Но обновится только тогда когда наш обьект инициализируется
            this.updateProfile();
        }
    }
</script>

<style scoped>
    img {
        max-width: 100%;
        height: auto;
    }
</style>