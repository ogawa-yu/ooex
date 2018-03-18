<template>
    <p>{{ greeting }} World!</p>
</template>

<script>
    module.export = {
        data: function() {
            return {
                greeting : 'Hello'
            }
        }
    }
</script>

<style scoped>
    p {
        font-size: 2em;
        font-align: center;
    }
</style>
