<template>
        <v-row no-gutters>
                <v-layout
                        class="pa-2"
                        outlined
                        tile
                >
                    <v-text-field
                            label="Add comment"
                            placeholder="Write something"
                            v-model="text"
                            @keyup.enter="save"
                    />
                    <v-btn
                            small
                            text
                            @click="save"
                            :disabled="isEmpty"
                    >
                        Add
                    </v-btn>
                </v-layout>
        </v-row>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        name: "CommentForm",
        props: ['messageId'],
        computed: {
            isEmpty() {
                const t = this.text
                return t === "" || t.trim().length === 0
            }
        },
        data() {
            return {
                text: ''
            }
        },
        methods: {
            ...mapActions(['addCommentAction']),
            async save() {
                await this.addCommentAction({
                    text: this.text,
                    message: {
                        id: this.messageId
                    }
                });
                this.text = ''
            }
        }
    }
</script>

<style scoped>

</style>