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