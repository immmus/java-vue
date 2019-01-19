<template>
    <v-layout align-space-between justify-start column fill-height>
        <message-form :messages="messages" :messageAttr="message"/>
        <message-row v-for="message in sortedMessages"
                     :key="message.id"
                     :message="message"
                     :editMessage="editMessage"
                     :deleteMessage="deleteMessage"
                     :messages="messages"/>
    </v-layout>
</template>

<script>
    import MessageRow from './MessageRow.vue'
    import MessageForm from './MessageForm.vue'
    import messagesApi from '../../api/messages'

    export default {
        props: ['messages'],
        computed: {
            sortedMessages() {
                return this.messages.sort((a, b) => -(a.id - b.id))
            }
        },
        components: {
            MessageRow,
            MessageForm
        },
        data() {
            return {
                message: null
            }
        },
        methods: {
            editMessage(message) {
                this.message = message
            },
            deleteMessage(message) {
                // {id: message.id} передает не только id, но и объект
                messagesApi.remove(/*{id: message.id}*/ message.id).then(result => {
                    if (result.ok) {
                        this.messages.splice(message.id, 1)
                    }
                })
            }
        }
    }
</script>

<style>

</style>