<template>
    <v-layout row justify="center">
        <v-dialog v-model="dialog" persistent max-width="290">
            <v-card>
                <v-card-title class="headline">WARRING!</v-card-title>
                <v-card-text>You are really want delete message?</v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn
                            text small flat
                            @click="deleteMessage"
                            @click.native="close"
                    >Yes</v-btn>
                    <v-btn
                            text small flat
                            @click.native="close"
                    >No</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-layout>
</template>

<script>
    export default {
        name: "ConfirmationDialog",
        props: ['del', 'message', 'dialog'],
        methods: {
            close() {
                // обновляем диалоговое окно
                this.$emit('update:dialog', false)
            },
            deleteMessage() {
                this.del(this.message)
            }
        }
    }
</script>

<style scoped>

</style>