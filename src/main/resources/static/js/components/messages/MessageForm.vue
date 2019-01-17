<template>
    <v-layout row>
        <v-text-field label="New message" placeholder="Write something" v-model="text"/>
        <v-btn @click="save" normal>
            Save
        </v-btn>
    </v-layout>
</template>

<script>
    import messagesApi from 'api/messages';

    export default {
        props: ['messages', 'messageAttr'],
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
            save() {
                const message = {
                    id: this.id,
                    text: this.text,
                   author: this.author
                };

                if (this.id) {
                    messagesApi.update(message).then(result =>
                        result.json().then(data => {
                            const index = this.messages.findIndex(item => item.id === data.id);
                            this.messages.splice(index, 1, data)
                        })
                    )
                } else {
                    messagesApi.add(message).then(result =>
                        result.json().then(data => {
                            const index = this.messages.findIndex(item => item.id === data.id);

                            if (index > -1) {
                                this.messages.splice(index, 1, data)
                            } else {
                                this.messages.push(data)
                            }
                        })
                    )
                }
                //После отправки сообщения нужно отчистить поля
                this.text = '';
                this.id = '';
                this.author = ' '
            }
        }
    }
</script>

<style>

</style>