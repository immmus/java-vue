<template>
    <v-container>
        <v-layout align-space-between justify-start column fill-height>
            <message-form :messageAttr="message"/>
            <message-row v-for="message in sortedMessages"
                         :key="message.id"
                         :message="message"
                         :editMessage="editMessage"/>
            <!--данный тег это vue компонент /components/LazyLoader.vue-->
            <lazy-loader></lazy-loader>
        </v-layout>
    </v-container>
</template>

<script>
    import {mapGetters} from 'vuex'
    import MessageRow from '../components/messages/MessageRow.vue'
    import MessageForm from '../components/messages/MessageForm.vue'
    import LazyLoader from '../components/LazyLoader.vue'

    export default {
        computed: mapGetters(['sortedMessages']),
        components: {
            LazyLoader,
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
            }
        }
    }
</script>

<style>

</style>