<template>
    <v-app>
        <div>
            <v-app-bar
                    :extended="extended"
                    :prominent="prominent"
                    :dense="dense"
                    :collapse="collapse"
                    :flat="flat"
                    :extension-height="extensionHeight">
                <v-toolbar-title>TestVue</v-toolbar-title>
                <!--двоеточие означает, что мы будем использовать его динамически.
                А $route.path показывает текущее положение, значит disabled отключает кнопку,
                когда мы находимся на странице по пути '/' -->
                <v-btn text
                       v-if="isAdmin"
                       :disabled="$route.path === '/admin_panel'"
                       @click="showAdminPanel">
                    Admin panel
                </v-btn>
                <v-btn text
                       v-if="profile"
                       :disabled="$route.path === '/'"
                       @click="showMessages">
                    Messages
                </v-btn>
                <v-spacer></v-spacer>
                <v-btn text
                       v-if="profile"
                       :disabled="$route.path === '/user'"
                       @click="showProfile">
                    {{profile.name}}
                </v-btn>
                <v-btn v-if="profile" icon href="/logout">
                    <v-icon>exit_to_app</v-icon>
                </v-btn>
            </v-app-bar>
        </div>
        <v-content>
            <router-view></router-view>
        </v-content>
    </v-app>
</template>

<script>
    import {mapMutations, mapGetters, mapState} from 'vuex'
    import {addHandler} from "../util/ws"

    export default {
        data: () => ({
            fixed: true,
            extended: false,
            extendedSlot: false,
            prominent: false,
            dense: false,
            collapse: false,
            flat: false,
            bg: false,
            extensionHeight: 48,
        }),
        computed: {
            ...mapState({profile: state => state.profile}),
            ...mapGetters({isAdmin: 'isAdmin'})
        },

        methods: {
            ...mapMutations([
                'addMessageMutation',
                'updateMessageMutation',
                'removeMessageMutation',
                'addCommentMutation'
            ]),
            showMessages() {
                this.$router.push('/')
            },
            showProfile() {
                this.$router.push('/user')
            },
            showAdminPanel() {
                this.$router.push('/api/admin_panel')
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
                } else if (data.objectType === 'COMMENT') {
                    if (data.eventType === 'CREATE') {
                        this.addCommentMutation(data.body);
                    } else {
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