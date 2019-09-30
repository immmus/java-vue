<template>
    <div class="container">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th> ID</th>
                <th> Email</th>
                <th> Last Visit</th>
                <th> ROLES</th>
                <th> isBanned</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="user in users">
                <td>{{user.id}}</td>
                <td>{{user.email}}</td>
                <td>{{user.lastVisit}}</td>
                <td>{{user.roles}}</td>
                <td>{{user.isBanned}}</td>
            </tr>
            </tbody>
        </table>

    </div>
</template>

<script>
    import adminPanel from "../api/adminPanel.js";

    export default {
        name: "AdminPanel",
        data: () => ({
                headers: [
                    {
                        text: 'NAME',
                        align: 'left',
                        sortable: false,
                        value: 'name',
                    },
                    {text: 'ID', value: 'id'},
                    {text: 'Email', value: 'email'},
                    {text: 'Last Visit', value: 'lastVisit'},
                    {text: 'ROLES', value: 'roles'},
                    {text: 'isBanned', value: 'isBanned'},
                ],
                users: []
            }
        ),
        async beforeMount() {
            const response = await adminPanel.getUsers()
            this.users = response.body.users
            /*.then(response => JSON.parse(response.body))
            .forEach(user => this.users.add({
                id: user.id,
                name: user.name,
                email: user.email,
                last_visit: user.lastVisit,
                roles: user.roles,
                banned: user.isBanned
            }))*/
        }
    }
</script>

<style scoped>

</style>