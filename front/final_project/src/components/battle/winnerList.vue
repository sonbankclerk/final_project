<template>
winnerList
<div>
  <div v-if ="list.length == 0">
    <notYet></notYet>
  </div>
  <div v-else>
    <div v-for="(winner,i) in list" :key="i">
      batnum : {{ winner.batnum }} |
      gender : {{ winner.gender }} |
      theme : {{ winner.theme }} |
      roundcnt : {{ winner.roundcnt }} |
      img : <img :src="'http://localhost:8081/battles/imgs/' + winner.batnum" alt="이미지 불러오기 실패"> |
      <div> {{ winner.batnum }}</div>
      vote : {{ winner.vote }} |
      winner : {{ winner.memnum }} 
    </div>
  </div>
</div>
</template>

<script>
import notYet from './notYet.vue'

export default{
  name:'winnerList',
  data(){
    return{
      dto:{},
      list:[]
    }
  },
  components : {
    notYet : notYet
  },
  created: function(){
    let self = this;
    self.$axios.get('http://localhost:8081/battles/winnerlist')
    .then(res =>{
      if(res.status == 200){
        this.list = res.data.list;
        console.log(this.list);
      }else{
        alert("오류로 인해 명예의 전당 활성화 불가")
      }
    })
  }
}
</script>