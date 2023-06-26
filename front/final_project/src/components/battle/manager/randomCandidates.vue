<template>
  <input type="button" value="랜덤 두명 뽑기" @click="find">
  <div v-show="show">
    {{ firstCandidate }}
    <img :src="'http://localhost:8081/battles/imgs/'+firstCandidate.batnum" alt="첫 번째">
    {{ secondCandidate }}
    <img :src="'http://localhost:8081/battles/imgs/'+secondCandidate.batnum" alt="두 번째">
  </div>
  <input type="button" value="후보 확정 하기" @click="firm">
</template>

<script>
export default{
  name : 'randomCandidates',
  data(){
    return{
      firstCandidate : {},
      secondCandidate : {},
      show : false
    }
  },
  methods:{
    find : function(){
      let self = this;

      self.$axios.get('http://localhost:8081/battles/manager/random')
      .then(res => {
        if(res.status == 200){
          if(res.data.len != 2){
            alert('아직 신청자의 수가 충분하지 않습니다.');
          }else{
            self.firstCandidate = res.data.list.pop();
            self.secondCandidate = res.data.list.pop();
            self.show = true;
          }
        }else{
          alert('오류로 인해 후보자 랜덤 뽑기 실행 불가.')
        }
      })
    },
    firm : function(){
      let self = this;
      let form = new FormData();
      if(!self.show){
        alert('랜덤으로 뽑힌 후보자가 없습니다.');
      }else{

        form.append("num1",self.firstCandidate.batnum);
        form.append("num2",self.secondCandidate.batnum);
        
        alert(self.firstCandidate.batnum)
        alert(self.secondCandidate.batnum)
        
        alert(`http://localhost:8081/battles/manager/random/${self.firstCandidate.batnum}/${self.secondCandidate.batnum}`);
        self.$axios.delete(`http://localhost:8081/battles/manager/random/${self.firstCandidate.batnum}/${self.secondCandidate.batnum}`)
        .then(res => {
          if(res.status == 200){
            alert('후보 확정');
            location.href = "/";  
          }else{
            alert('오류로 인해 후보자 확정 불가.')
          }
        })
      }
      
    }
  }
}
</script>