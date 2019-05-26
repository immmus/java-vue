<template>
    <!--Ссылка на документаци по отступам
            https://vuetifyjs.com/ru/framework/spacing -->
    <v-card class="my-2">
        <v-card-text primary-title>
            <!--https://vuetifyjs.com/ru/components/avatars-->
            <div>
                <v-avatar
                        v-if="message.author && message.author.userPicture"
                        size="48px"
                >
                    <img
                            :src="message.author.userPicture"
                            :alt="message.author.name"
                    >
                </v-avatar>

                <v-avatar
                        v-else
                        size="36px"
                        color="indigo"
                >
                    <v-icon dark>account_circle</v-icon>
                </v-avatar>
                <span class="pl-3">{{ authorName }}</span>

                <v-layout align-center justify-end>
                    <v-card-actions>
                        <v-btn @click="edit" small flat round>Edit</v-btn>
                        <v-btn icon @click="del" small>
                            <v-icon>delete</v-icon>
                        </v-btn>
                    </v-card-actions>
                </v-layout>
            </div>
            <div class="pl-3">
                {{ message.text }}
            </div>
        </v-card-text>

        <media v-if="message.link" :message="message"></media>

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

    export default {
        name: "MessageRow",
        props: ['message', 'editMessage'],
        components: {CommentList, Media},
        // https://ru.vuejs.org/v2/guide/computed.html
        computed: {
            // данным методом мы обработали ситуацию - если вдруг автор не задан
            authorName() {
                return this.message.author ? this.message.author.name : 'unknown'
            }
        },
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