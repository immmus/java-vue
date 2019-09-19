<template>
    <!--https://vuetifyjs.com/ru/components/lists-->
    <v-list-tile>
        <!--указываем автора и размер аватара для компонента UserLink-->
        <user-link
                :user ="comment.author"
        ></user-link>
        <v-list-tile-content>
            <v-list-tile-title>{{comment.text}}</v-list-tile-title>
        </v-list-tile-content>
    </v-list-tile>
</template>

<script>
    import UserLink from "../UserLink.vue";
    export default {
        name: 'CommentItem',
        components: {UserLink},
        props: ['comment']
    }
</script>

<style scoped>
</style>