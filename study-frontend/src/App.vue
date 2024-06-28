<script setup>

import {get} from "./net";
import {useStore} from "./stores";
import router from "./router";

const store = useStore()

// console.log(store.auth.user)
//   get('/api/user/me',(message) =>{
//   store.auth.user = message
//   router.push('/index/index1')
// },() =>{
//   store.auth.user = null
// })
</script>

<template>
  <router-view/>
</template>

<style scoped>

</style>
