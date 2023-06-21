<template>
  <div v-if="chk">
    {{ winner }}
  </div>
  <div v-else>
    <notYet></notYet>
  </div>
</template>

<script>
import notYet from '@/components/battle/notYet.vue'


export default {
  data(){
    return{
      dto : {},
      winner : {},
      chk : true
    }
  },
  created:function(){
    let self = this;
    // self.$axios.get(`http://localhost:8081/members/${this.memnum}`)
    // .then(res =>{
    //   if(res.status == 200){
    //       this.dto = res.data.dto;
    //   }else{
    //     alert("오류 발생으로 인한 로그인 정보 불러오기 실패")
    //   }
    // })
        
    self.$axios.get("http://localhost:8081/battles/winner")
    .then(res =>{
      if(res.status == 200 || res.data.flag){
        alert(res.data.dto);
        this.winner = res.data.dto;
      }else{
        this.chk = false;
      }
    })
  },
  components: {
    notYet
  }
}
</script>