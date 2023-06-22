<template >
    <div>
        <router-link to="/addBoard" >게시글 등록</router-link>
        <br>
        <input type="search" v-model="searchQuery" placeholder="#오피스룩 #패피">
        <button @click="search">검색</button>
    </div>
</template>
<script>
export default {
    data(){
        return{
            
        }
    },
    method: {
        
    }
}
</script>
<style>
    
</style>