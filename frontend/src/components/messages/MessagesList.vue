<template>
    <v-layout align-space-between justify-start column fill-height>
        <message-form :messageAttr="message"/>
        <message-row v-for="message in sortedMessages"
                     :key="message.id"
                     :message="message"
                     :editMessage="editMessage"/>
    </v-layout>
</template>

<script>
    import { mapGetters } from 'vuex'
    import MessageRow from './MessageRow.vue'
    import MessageForm from './MessageForm.vue'

    export default {
        computed: mapGetters(['sortedMessages']),
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
        }
    }
</script>

<style>

</style>