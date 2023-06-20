<template>
  theme : {{ theme }}
  <input type="file">
</template>

<script>
export default {
  date(){
    return{
      theme:'',
      memnum: sessionStorage.getItem("memnum"),
      gender: ''
    }
  },
  created: function(){
    const self = this;
  },
  methods:{
    applyBattle(){
      this.$axios.post('http://localhost:8081')
    }
  }
}
</script>

<style>

</style>