<style>
    .my-component h2 {
        color: red
    }
</style>
<template>
    <div class="my-component">
        <h2>{{message}}</h2>
    </div>
</template>
<script>
    module.exports = {
        data: function() {
            return {
                message: 'Hello Vue Component!!'
            }
        }
    }
</script>

