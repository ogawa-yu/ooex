<style>
    .my-component h2 {
        color: red
    }
</style>
<template>
    <div class="my-component">
        <h2>Hello {{message}}</h2>
    </div>
</template>
<script>
    module.exports = {
        data: function() {
            return {
                message: 'Vue Component!!'
            }
        }
    }
</script>

