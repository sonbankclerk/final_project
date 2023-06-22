<template>
winnerList
<div>
  <div v-for="(winner,i) in list" :key="i">
    batnum : {{ winner.batnum }} |
    gender : {{ winner.gender }} |
    theme : {{ winner.theme }} |
    roundcnt : {{ winner.roundcnt }} |
    img : <img src="'http://localhost:8081/battles/imgs/' + winner.batnum" alt="이미지 불러오기 실패"> |
    vote : {{ winner.vote }} |
    winner : {{ winner.memnum }} 
  </div>
</div>
</template>

<script>
export default{
  name:'winnerList',
  data(){
    return{
      dto:{},
      list:[]
    }
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