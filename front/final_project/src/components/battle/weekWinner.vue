<template>
  <div v-if="chk">
    {{ winner }}
    <img :src="'http://localhost:8081/battles/imgs/'+winner.batnum" alt="">
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
    
    // chk는 혹시나 오류가 났을 때 대처할 페이지로 이동의 유무를 따지는 역할을 한다.
    self.$axios.get("http://localhost:8081/battles/winner")
    .then(res =>{
      if(res.status == 200 || res.data.flag){
        if(res.data.dto == undefined){
          this.chk = false;
        }
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