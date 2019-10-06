<template>
    <v-row justify="center">
        <v-dialog v-model="dialog"
                  fullscreen
                  transition="dialog-bottom-transition"
        >
            <v-card>
                <v-app-bar>
                    <v-toolbar-title>User Messages</v-toolbar-title>
                    <div class="flex-grow-1"></div>
                    <v-toolbar-items>
                        <v-btn text @click.native="close">
                            CLOSE
                        </v-btn>
                    </v-toolbar-items>
                </v-app-bar>
                <messages-list></messages-list>
            </v-card>
        </v-dialog>
    </v-row>
</template>

<script>
    import MessagesList from "../../pages/MessagesList.vue";

    export default {
        name: "UserMessagesList",
        components: {MessagesList},
        props: ['dialog'],
        methods: {
            close() {
                // обновляем диалоговое окно
                this.$emit('update:dialog', false)
            }
        }
    }
</script>

<style scoped>

</style>