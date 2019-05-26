<template>
    <v-layout row>
        <v-text-field
                label="New message"
                placeholder="Write something"
                v-model="text"
                @keyup.enter="save"
        />
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
                    id: this.id,
                    text: this.text,
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