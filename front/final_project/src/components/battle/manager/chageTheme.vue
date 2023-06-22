<template>
  <input type="text" v-model="theme">
  <input type="button" value="변경 하기" @click="changeTheme">
  This week's theme is {{ theme }}

</template>

<script>
export default{
  name:'ChangeTheme',
  data(){
    return{
      theme : null
    }
  },
  methods:{
    changeTheme : function(){
      let self = this;
      let form = new FormData();
      form.append('theme',this.theme);

      self.$axios.put('http://localhost:8081/battles/manager',form)
      .then(res => {
        if(res.status == 200){
          if(res.data.flag){
            alert("테마 변경 완료.");
          }
        }else{
          alert('오류로 인해 테마 변경 불가.')
        }
      })
    }
  }
}
</script>