<!--Этот компонент вызывается в MessagesLis.vue-->
<template>
    <span></span>
</template>

<!--Заиспользуем хук жизненного цикла компонента
    При создании нашего компонента, во время его отображения не экране.
    Будем навешивать обработчик, который будет отслеживать прокрутку.
    И перед тем как компонент будет уничтожаться нам надо будет этот обработчик отписывать.
    Чтобы он не захламлял память.

    https://ru.vuejs.org/v2/guide/instance.html#Хуки-жизненного-цикла-экземпляра
     Обработчик мы будем добавлять во время "mounted".
     А отписывать во время "beforeDestroy".
    -->
<script>
    // импортируем экшины, чтобы запустить загрузку при выполнении условия isBottomOfScreen
    import {mapActions} from 'vuex'
    import messagesApi from '../api/messages.js'
    import adminPanelApi from "../api/adminPanel.js"

    export default {
        name: "LazyLoader",
        // экшины надо обязательно добавлять в methods
        methods: mapActions(['loadPageAction']),
        // Документация по добавлени и удалению обработчика
        // https://www.w3schools.com/jsref/event_onscroll.asp
        mounted() {
            window.onscroll = () => {
                const el = document.documentElement
                // проверяем что прокрутили страничку до конца экрана
                // Т.е. текущая позиция + высота окна больше общей высоты документа деленной на 1.05
                const isBottomOfScreen = (el.scrollTop + window.innerHeight) >= el.offsetHeight / 1.05
                if (isBottomOfScreen) {
                    const to = this.$route.path.includes('/admin_panel');
                    if (to) {
                       // const userId = this.$route.params.id
                        this.loadPageAction(adminPanelApi)
                    } else {
                        this.loadPageAction(messagesApi)
                    }
                }
            }
        },
        beforeDestroy() {
            window.onscroll = null
        }
    }
</script>

<style scoped>

</style>