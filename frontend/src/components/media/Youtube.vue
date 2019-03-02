<template>
    <div class="video-container">
    <iframe
            width="560"
            height="415"
            :src="targetSrc"
            frameborder="0"
            allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
            allowfullscreen
    ></iframe>
    </div>
</template>

<script>
    export default {
        name: "Youtube",
        props: ['src'],
        computed: {
            targetSrc() {
                let parts = this.src.split('/');
                return `https://www.youtube.com/embed/${parts[parts.length - 1]}`
            }
        }
    }
</script>

<style scoped>
    .video-container {
        position: relative;
        padding-bottom: 56.25%;
        padding-top: 30px;
        height: 0;
        overflow: hidden;
    }
    .video-container iframe {
        position: absolute;
        top:0;
        left: 0;
        width: 100%;
        height: 100%;
    }
</style>