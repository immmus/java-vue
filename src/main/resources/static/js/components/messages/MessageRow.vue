<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <i>({{ message.id }})</i> {{ message.text }}
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
    export default {
        name: "MessageRow",
        props: ['message', 'editMessage', 'messages', 'deleteMessage'],
        methods: {
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.deleteMessage(this.message)
            }
        }
    }
</script>

<style scoped>

</style>