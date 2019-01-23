<template>
    <v-container>
        <v-layout align-space-between justify-start column fill-height>
            <message-form :messageAttr="message"/>
            <message-row v-for="message in sortedMessages"
                         :key="message.id"
                         :message="message"
                         :editMessage="editMessage"/>
        </v-layout>
    </v-container>
</template>

<script>
    import {mapGetters} from 'vuex'
    import MessageRow from '../components/messages/MessageRow.vue'
    import MessageForm from '../components/messages/MessageForm.vue'

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