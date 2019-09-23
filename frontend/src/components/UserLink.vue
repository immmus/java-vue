<template>
    <!-- router-link это компонент из vue router
    про который можно почитать по ссылке https://router.vuejs.org/ru/guide/#html
    его параметр "to" это ссылкакуда должны перейти
    -->
    <router-link
            :to="`/user/${user.id}`"
    >
        <v-list-item>
            <!--28 это значение по дефолту-->
            <v-list-item-avatar
                    v-if="user && user.userPicture"
                    :size="`${size || 36}px`"
            >
                <v-img
                        :src="user.userPicture"

                ></v-img>
            </v-list-item-avatar>

            <v-list-item-avatar
                    v-else
                    :size="`${size || 36}px`"
                    color="black"
            >
                <v-icon dark>account_circle</v-icon>
            </v-list-item-avatar>

            <v-list-item-content>
                <v-list-item-title>
                    {{userName}}
                </v-list-item-title>

                <v-list-item-subtitle
                        v-if="creationDate"
                >
                    {{creationDate}}
                </v-list-item-subtitle>
            </v-list-item-content>
        </v-list-item>
        <!--<span class="px-2">{{ userName }}</span>-->
    </router-link>
</template>

<script>
    export default {
        name: "UserLink",
        // Эти параметры передаются в темплейтах MessageRow и CommentItem
        props: ['user', 'creationDate', 'size'],
        // https://ru.vuejs.org/v2/guide/computed.html
        computed: {
            // данным методом мы обработали ситуацию - если вдруг автор не задан
            userName() {
                return this.user ? this.user.name : 'unknown'
            }
        }
    }
</script>

<style scoped>

</style>