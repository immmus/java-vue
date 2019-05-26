<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <a flat
               :small="$vuetify.breakpoint.xsOnly">
                ({{ message.id }})
            </a>
            {{ message.text }}
        </v-card-text>

        <media v-if="message.link" :message="message"></media>

        <v-layout align-center justify-end>
            <v-card-actions>
                <v-btn @click="edit" small flat round>Edit</v-btn>
                <v-btn icon @click="del" small>
                    <v-icon>delete</v-icon>
                </v-btn>
            </v-card-actions>
            <comment-list
                    :comments="message.comments"
                    :message-id="message.id"
            ></comment-list>
        </v-layout>
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