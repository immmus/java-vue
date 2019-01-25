<template>
    <v-layout row>
        <v-text-field label="New message" placeholder="Write something" v-model="text"/>
        <v-btn @click="save" normal
                    :disabled="isEmpty">
            Save
        </v-btn>
    </v-layout>
</template>

<script>
    import { mapActions } from 'vuex'

    export default {
        props: ['messageAttr'],
        computed: {
            isEmpty(){
                const t = this.text
                return t === "" || t.trim().length === 0 || t ===null
            }
        },
        data: function () {
            return {
                author: '',
                text: '',
                id: ''
            }
        },
        watch: {
            messageAttr(newVal, oldVal) {
                this.author = newVal.author;
                this.text = newVal.text;
                this.id = newVal.id;
            }
        },
        methods: {
            ...mapActions(['updateMessageActions', 'addMessageActions']),
            save() {
                const message = {
                    id: this.id,
                    text: this.text,
                    author: this.author
                };

                if (this.id) {
                    this.updateMessageActions(message)
                } else {
                    this.addMessageActions(message)
                }
                //После отправки сообщения нужно отчистить поля
                this.text = '';
                this.id = '';
                this.author = ''
            }
        }
    }
</script>

<style>

</style>fkpsmh6clh3csorw43eaodlqvkn