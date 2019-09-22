<template>
    <!-- router-link это компонент из vue router
    про который можно почитать по ссылке https://router.vuejs.org/ru/guide/#html
    его параметр "to" это ссылкакуда должны перейти
    -->
    <router-link
            :to="`/user/${user.id}`"
    >
        <!--36 это значение по дефолту-->
        <v-avatar
                v-if="user && user.userPicture"
                :size="`${size || 36}px`"
        >
            <img
                    :src="user.userPicture"
                    :alt="user.name"
            >
        </v-avatar>

        <v-avatar
                v-else
                :size="`${size || 36}px`"
                color="black"
        >
            <v-icon dark>account_circle</v-icon>
        </v-avatar>
        <span class="px-2">{{ userName }}</span>
    </router-link>
</template>

<script>
    export default {
        name: "UserLink",
        // Эти параметры передаются в темплейтах MessageRow и CommentItem
        props: ['user', 'size'],
        // https://ru.vuejs.org/v2/guide/computed.html
        computed: {
            // данным методом мы обработали ситуацию - если вдруг автор не задан
            userName() {
                return this.user ? this.user.name : 'unknown'
            }
        },
    }
</script>

<style scoped>

</style>