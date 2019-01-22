<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <i>({{ message.author }})</i> {{ message.text }}
        </v-card-text>

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
    import { mapActions } from 'vuex'
    export default {
        name: "MessageRow",
        props: ['message', 'editMessage'],
        methods: {
            ...mapActions(['removeMessageActions']),
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

</style>