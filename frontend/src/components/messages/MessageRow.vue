<template>
    <!--Ссылка на документаци по отступам
            https://vuetifyjs.com/ru/framework/spacing -->
    <v-card class="my-2">
        <v-card-text primary-title>
            <!--указываем автора и размер аватара для компонента UserLink-->
            <user-link
                    :user ="message.author"
                    size="48"
            ></user-link>
            <div class="pl-3 pt-3">
                {{ message.text }}
            </div>
        </v-card-text>

        <media v-if="message.link" :message="message"></media>

        <v-layout align-center justify-end>
            <v-card-actions>
                <v-btn @click="edit" small flat round>Edit</v-btn>
                <v-btn icon @click="del" small>
                    <v-icon>delete</v-icon>
                </v-btn>
            </v-card-actions>
        </v-layout>

        <comment-list
                :comments="message.comments"
                :message-id="message.id"
        ></comment-list>
    </v-card>
</template>

<script>
    import {mapActions} from 'vuex'
    import Media from '../media/Media.vue'
    import CommentList from "../comment/CommentList.vue";
    import UserLink from "../UserLink.vue";

    export default {
        name: "MessageRow",
        props: ['message', 'editMessage'],
        components: {UserLink, CommentList, Media},
        methods: {
            ...mapActions(['removeMessageAction']),
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.removeMessageAction(this.message)
            }
        }
    }
</script>

<style scoped>
    a {
        color: #000 !important;
    }
</style>