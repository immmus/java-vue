<template>
    <v-app>
        <v-toolbar app>
            <v-toolbar-title>TestVue</v-toolbar-title>
            <v-btn flat v-if="profile"
                   :disabled="$route.path === '/' "
                   @click="showMessages"> <!--двоеточие означает, что мы будем использовать его динамически.
                            А $route.path показывает текущее положение, значит disabled отключает кнопку,
                             когда мы находимся на странице по пути '/' -->
                Messages
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn flat
                   v-if="profile"
                   :disabled="$route.path === '/profile'"
                   @click="showProfile">
                {{profile.name}}
            </v-btn>
            <v-btn v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-toolbar>
        <v-content>
            <router-view></router-view>
        </v-content>
    </v-app>
</template>

<script>
    import {mapMutations, mapState} from 'vuex'
    import {addHandler} from "../util/ws"

    export default {
        computed: mapState(['profile']),
        methods: {
            ...mapMutations(['addMessageMutation', 'updateMessageMutation', 'removeMessageMutation']),
            showMessages() {
                this.$router.push('/')
            },
            showProfile() {
                this.$router.push('/profile')
            }
        },
        created() {
            addHandler(data => {
                if (data.objectType === 'MESSAGE') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addMessageMutation(data.body);
                            break;
                        case 'UPDATE':
                            this.updateMessageMutation(data.body);
                            break;
                        case 'REMOVE':
                            this.removeMessageMutation(data.body);
                            break;
                        default:
                            console.error('Looks like the event type if unknown "${data.eventType)}" ')
                    }
                } else {
                    console.error('looks like object type if unknown "${data.objectType}" ')
                }
            })
        },
        beforeMount() {
            if (!this.profile) {
                this.$router.replace('/auth')
            }
        }
    }
</script>

<style>
</style>