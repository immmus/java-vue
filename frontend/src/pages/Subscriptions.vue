<template>
    <v-container>
        <v-layout justify-space-around>
            <v-list>
                <!--https://vuetifyjs.com/ru/components/lists-->
                <v-list-tile
                        v-for="(item, index) in subscriptions"
                        :key="index"
                >
                    <user-link
                            :key="'user' + index"
                            :user ="item.subscriber"
                            size="24"
                    ></user-link>
                    <v-btn
                            :key="'btn' + index"
                            @click="changeSubscriptionStatus(item.subscriber.id)"
                    >
                        {{item.active ? 'Dismiss' : 'Approve'}}
                    </v-btn>
                </v-list-tile>
            </v-list>
        </v-layout>
    </v-container>
</template>

<script>
    import profileApi from "../api/profile.js";
    import UserLink from "../components/UserLink.vue";
    export default {
        name: "Subscriptions",
        components: {UserLink},
        data() {
            return {
                subscriptions: []
            }
        },
        methods: {
            async changeSubscriptionStatus(subscriberId) {
                await profileApi.changeSubscriptionStatus(subscriberId)
                const subscriptionIndex = this.subscriptions.findIndex(item =>
                    item.subscriber.id === subscriberId
                )
                const subscription = this.subscriptions[subscriptionIndex]
                this.subscriptions = [
                    ...this.subscriptions.splice(0, subscriptionIndex),
                    {
                        ...subscription,
                        active: !subscription.active
                    },
                    ...this.subscriptions.slice(subscriptionIndex + 1)
                ]
            }
        },
        // async потому что дергаем api
        async beforeMount() {
            const resp = await profileApi.subscriberList(this.$store.state.profile.id)
            this.subscriptions = await resp.json()
        }
    }
</script>

<style scoped>

</style>