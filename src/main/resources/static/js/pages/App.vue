<template>
    <v-app>
        <v-toolbar app>
            <v-toolbar-title>TestVue</v-toolbar-title>
            <v-spacer></v-spacer>
            <span v-if="profile">{{profile.name}}</span>
            <v-btn v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-toolbar>
        <v-content>
            <v-container v-if="!profile">Need Authorize
                <a href="/login">Google</a>
            </v-container>
            <v-container v-if="profile">
                <messages-list :messages="messages"/>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import MessagesList from "components/messages/MessagesList.vue"
    import {addHandler} from "util/ws";
    import {getIndex} from "util/collections";

    export default {
        components: {
            MessagesList
        },
        data() {
            return {
                messages: frontendData.messages,
                profile: frontendData.profile
            }
        },
        created() {
            addHandler(message => {
                //получаем индех сообщения в листе
                let index = getIndex(this.messages, message.id)
                if (index > -1) {
                    //заменяем существующее сообщение по индексу, указываем, что елемент будет один
                    //и передаем наше сообщение
                    this.messages.splice(index, 1, message)
                } else {
                    //если сообщение новое отправляем его на сервер
                    this.messages.push(message)
                }
            })
        }
    }
</script>

<style>
</style>