<template>
  <input type="button" value="랜덤 두명 뽑기" @click="find">
  {{ firstCandidate }}
  {{ secondCandidate }}
  <input type="button" value="후보 확정 하기" @click="firm">
</template>

<script>
export default{
  name : 'randomCandidates',
  data(){
    return{
      firstCandidate : {},
      secondCandidate : {}
    }
  },
  methods:{
    find : function(){
      let self = this;

      self.$axios.get('http://localhost:8081/battles/manager/random')
      .then(res => {
        if(res.status == 200){
          this.firstCandidate = res.data.list.pop();
          this.secondCandidate = res.data.list.pop();
        }else{
          alert('오류로 인해 후보자 랜덤 뽑기 실행 불가.')
        }
      })
    },
    firm : function(){
      let self = this;
      let form = new FormData();

      form.append("num1",this.firstCandidate.batnum);
      form.append("num2",this.secondCandidate.batnum);

      self.$axios.delete('http://localhost:8081/battles/manager/random',form)
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
</script>