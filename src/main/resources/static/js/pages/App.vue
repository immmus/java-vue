<template>
    <div>
        <div v-if="!profile">Need Authorize
            <a href="/login">Google</a>
        </div>
        <div v-else>
            <div>{{profile.name}}&nbsp;
                <a href="/logout">Logout</a>
            </div>
            <messages-list :messages="messages"/>
        </div>
    </div>
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