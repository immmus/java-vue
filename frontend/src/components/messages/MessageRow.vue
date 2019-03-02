<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <a flat
                   :small="$vuetify.breakpoint.xsOnly"
                   @click="showMessageAuthorProfile">
                ({{ message.author }})
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
        </v-layout>
    </v-card>
</template>

<script>
    import {mapActions} from 'vuex'
    import Media from '../media/Media.vue'
    export default {
        name: "MessageRow",
        props: ['message', 'editMessage'],
        components: {Media},
        methods: {
            ...mapActions(['removeMessageActions']),
            showMessageAuthorProfile() {
                this.$router.push('/user/' + this.author)
            },
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.removeMessageActions(this.message)
            }
        }
    }
</script>

<style scoped>
    a {
        color: #000 !important;
    }
</style>