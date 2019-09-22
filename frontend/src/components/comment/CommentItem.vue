<template>
    <!--https://vuetifyjs.com/ru/components/lists-->
    <v-card>
        <v-card-text primary-title>
            <!--указываем автора и размер аватара для компонента UserLink-->
            <user-link
                    :user="comment.author"
                    :creation-date="comment.creationDate"
            ></user-link>


            <div
                    class="pl-3 pt-3"
            >
                {{comment.text}}
            </div>
        </v-card-text>
    </v-card>
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
    a {
        color: #233d54 !important;
        text-decoration: none;
    }
</style>