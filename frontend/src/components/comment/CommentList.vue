<template>
    <v-card>
        <v-card-text v-if="comments.length > 0">
            <v-subheader>
                Comments
            </v-subheader>
            <template
                    v-for="(item, index) in comments"
            >
                <!--Это полосочка которая разделяет комменты. Ее мы будем показывть начиная со второго коммента.-->
                <!--Так же необходима задать ключ-->
                <v-divider
                        v-if="index > 0"
                        :key="index"
                ></v-divider>

                <!--Так как ключ index у нас уже занят мы берем елемент item и добавляем к нему index-->
                <comment-item
                        :comment="item"
                        :key="'item' + index"
                ></comment-item>
            </template>
        </v-card-text>
        <v-card-actions
                align-end
                justify-start
        >
            <comment-form :message-id="messageId"></comment-form>
        </v-card-actions>
    </v-card>
</template>
<script>
    import CommentForm from './CommentForm.vue'
    import CommentItem from './CommentItem.vue'

    export default {
        name: 'CommentList',
        components: {CommentForm, CommentItem},
        props: ['comments', 'messageId']
    }
</script>

<style scoped>
</style>