<template>
    <v-dialog
            :fullscreen="$vuetify.breakpoint.xsOnly"
            v-model="dialog"
            scrollable
            persistent
            max-width="380px">
        <v-card>
            <v-card-title>Subscriptions</v-card-title>
            <v-divider></v-divider>
            <v-card-text style="height: 480px;">
                <!--https://vuetifyjs.com/ru/components/lists-->
                <v-list>
                    <v-item-group column
                                  v-for="item in subscriptions"
                                  :key="item.id"
                    >
                        <v-flex
                                grow
                                pa-1
                        >
                            <v-card>
                                <v-card-text>
                                    <user-link
                                            @click.native="close"
                                            :user="item.subscriber"
                                            size="24"
                                    ></user-link>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn
                                            text
                                            small
                                            color="indigo"
                                            @click="changeSubscriptionStatus(item.subscriber.id)"
                                    >
                                        <v-icon v-if="item.active" dark>remove</v-icon>
                                        <v-icon v-else dark>add</v-icon>
                                        {{item.active ? 'dismiss sub' : 'approve sub'}}
                                    </v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-flex>
                    </v-item-group>
                </v-list>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
                <v-btn block text @click.native="close">Close</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import profileApi from "../api/profile.js";
    import UserLink from "../components/UserLink.vue";

    export default {
        name: "Subscriptions",
        props: ['dialog'],
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
                    ...this.subscriptions.slice(0, subscriptionIndex),
                    {
                        ...subscription,
                        active: !subscription.active
                    },
                    ...this.subscriptions.slice(subscriptionIndex + 1)
                ]
            },
            async loadSubscriptions() {
                const resp = await profileApi.subscriberList(this.$store.state.profile.id)
                this.subscriptions = await resp.json()
            },
            close() {
                // обновляем диалоговое окно
                this.$emit('update:dialog', false)
            }
        },
        // async потому что дергаем api
        async beforeMount() {
            this.loadSubscriptions()
        }
    }
</script>

<style scoped>
    a {
        color: #000 !important;
        text-decoration: none;
    }
</style>