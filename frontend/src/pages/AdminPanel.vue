<template>
    <v-card>
        <v-card-title>
            Users
            <div class="flex-grow-1"></div>
            <v-text-field
                    v-model="search"
                    append-icon="search"
                    label="Search"
                    single-line
                    hide-details
            ></v-text-field>
        </v-card-title>
        <v-data-table
                :headers="headers"
                :items="users"
                :search="search"
        >
            <template v-slot:item.isBanned="{item}">
                <v-switch
                        v-model="item.isBanned"
                        :label="`${item.isBanned}`"
                        color="#f50057"
                        @change="banOrUnban(item.id)"
                ></v-switch>
            </template>
            <template v-slot:item.link="{item}">
                <user-messages-list
                        :pageMessages.sync="pageMessages"
                        :dialog.sync="dialog"
                ></user-messages-list>
                <v-btn x-small
                       color="teal"
                       @click.stop="getMess(item.id)"

                >open messages</v-btn>
            </template>
            <template v-slot:item.userLink="{item}">
                <user-link
                        class="user-link"
                        :user="item"
                        :creation-date="''"
                ></user-link>
            </template>
        </v-data-table>
    </v-card>
</template>

<script>
    import {mapActions} from 'vuex'
    import adminPanel from "../api/adminPanel.js"
    import UserLink from "../components/UserLink.vue"
    import UserMessagesList from "../components/adminPanel/UserMessagesList.vue";

    export default {
        name: "AdminPanel",
        components: {UserLink, UserMessagesList},
        data: () => ({
                headers: [
                    {
                        text: 'Profile',
                        align: 'left',
                        sortable: false,
                        value: 'userLink',
                    },
                    {text: 'ID', value: 'id'},
                    {text: 'Email', value: 'email'},
                    {text: 'Last Visit', value: 'lastVisit'},
                    {text: 'ROLES', value: 'roles'},
                    {text: 'Banned', value: 'isBanned', sortable: false},
                    {text: 'Messages', value: 'link', sortable: false}
                ],
                dialog: false,
                pageMessages: [],
                users: [],
                search: ''
            }
        ),
        methods: {
            ...mapActions(['loadPageMessageForAdmin']),
            async banOrUnban(userId) {
                await adminPanel.banOrUnban(userId)
            },
            getMess(userId) {
                this.loadPageMessageForAdmin(userId)
                this.dialog = true
            }
        },
        async beforeMount() {
            const response = await adminPanel.getUsers()
            this.users = response.body.users
        }
    }
</script>

<style scoped>
    .user-link {
        color: #000 !important;
        text-decoration: none;
    }
</style>