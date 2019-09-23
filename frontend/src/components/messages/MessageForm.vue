<template>
    <v-container>
        <v-row>

            <v-text-field
                    label="New message"
                    placeholder="Write something"
                    v-model="text"
                    @keyup.enter="save"
            >
            </v-text-field>
            <v-btn
                    text
                    @click="save"
                   normal
                   :disabled="isEmpty"
            >Send
            </v-btn>
        </v-row>
    </v-container>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        props: ['messageAttr'],
        computed: {
            isEmpty() {
                const t = this.text
                return t === "" || t.trim().length === 0
            }
        },
        data: function () {
            return {
                text: '',
                id: null
            }
        },
        watch: {
            messageAttr(newVal, oldVal) {
                this.text = newVal.text;
                this.id = newVal.id;
            }
        },
        methods: {
            ...mapActions(['updateMessageAction', 'addMessageAction']),
            save() {
                const message = {
                    text: this.text,
                    id: this.id,
                };

                if (this.id) {
                    this.updateMessageAction(message)
                } else {
                    this.addMessageAction(message)
                }
                //После отправки сообщения нужно отчистить поля
                this.text = '';
                this.id = null
            }
        }
    }
</script>

<style>

</style>